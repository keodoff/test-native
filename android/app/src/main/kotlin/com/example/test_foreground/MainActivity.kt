package com.example.test_foreground

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


class MainActivity: FlutterActivity() {
    private val CHANNEL = "hello"

    val foregoundService = MyCameraService();

    @RequiresApi(Build.VERSION_CODES.R)
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
                call, result ->
            if (call.method == "startForeground") {
              val rez  =   foregoundService.startForeground()
                result.success("start: $rez")
            }

            if(call.method == "stopForeground"){
                val rez  =     foregoundService.stopService()
                result.success("foreground stop $rez ")
            }
        }
    }




}

class MyCameraService: Service() {


    private    val NOTIFICATION_ID: Int = 1

    @RequiresApi(Build.VERSION_CODES.R)
    fun startForeground() : Boolean{
        // Before starting the service as foreground check that the app has the
        // appropriate runtime permissions. In this case, verify that the user has
        // granted the CAMERA permission.
//        val cameraPermission =
//            PermissionChecker.checkSelfPermission(this, Manifest.permission.)
//        if (cameraPermission != PermissionChecker.PERMISSION_GRANTED) {
//            // Without camera permissions the service cannot run in the foreground
//            // Consider informing user or updating your app UI if visible.
//            stopSelf()
//            return
//        }

        try {
            val notification = NotificationCompat.Builder(this, "ForegroundServiceChannelId")

                .setContentTitle("Foreground Service")
                .setContentText("Foreground service is running")
                .build()
            // Use ServiceCompat to start the foreground service with type:
            ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA);


            return true;
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
            }
            // ...
        }
        return false;
    }

      fun stopService(): Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH);
            return true;
        } else {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE);
            return true;
        }

    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}

