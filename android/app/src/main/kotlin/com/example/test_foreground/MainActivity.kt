package com.example.test_foreground

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Camera
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.SurfaceHolder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodChannel

import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraManager
import android.os.Bundle


import android.os.Environment

import android.view.SurfaceView


import java.io.File

class MainActivity : FlutterActivity(), SurfaceHolder.Callback {
    private val CHANNEL = "hello"
    private var mediaRecorder: MediaRecorder? = null
    private var camera: Camera? = null
    lateinit var engine: FlutterEngine
    private var isRecording = false

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        engine = flutterEngine
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            if (call.method == "startForeground") {
                val argument = call.arguments as String
                Log.e("ARGUMENT", "ARG: $argument")
                val rez = MyCameraService().startForeground(argument)
                result.success("start: $rez")
            }

            if (call.method == "stopForeground") {
                val rez = MyCameraService().stopService()
                result.success("foreground stop $rez ")
            }
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder) {}
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {}

//    @RequiresApi(Build.VERSION_CODES.N)
//    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
//
//
//        MethodChannel(
//            engine.dartExecutor.binaryMessenger,
//            CHANNEL
//        ).setMethodCallHandler { call, result ->
//            when (call.method) {
//
//                "startForeground" -> {
//
//                    val argument = call.arguments as String
//                    Log.e("ARGUMENT", "ARG: $argument")
//                    val rez = startRecording(argument)
//                    result.success("start rez: $rez")
//
//                }
//
//                "stopForeground" -> {
//                    val rez = stopRecording()
//                    result.success("Stopped rez: $rez")
//                }
//
//                else -> result.notImplemented()
//            }
//        }
//    }




    @RequiresApi(Build.VERSION_CODES.N)
    private fun startRecording(filePath: String) {
        if (isRecording) return

        try {
            camera = Camera.open()
            val surfaceTexture = SurfaceTexture(10)
            camera?.setPreviewTexture(surfaceTexture)
            camera?.unlock()

            mediaRecorder = MediaRecorder().apply {
                setCamera(camera)
                setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                setVideoSource(MediaRecorder.VideoSource.CAMERA)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                setOutputFile(filePath)

                prepare()
                start()
            }

            isRecording = true
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun stopRecording() {
        if (!isRecording) return

        try {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        camera?.apply {
            stopPreview()
            lock()
            release()
        }

        mediaRecorder = null
        camera = null
        isRecording = false
    }


}

class MyCameraService : Service() {
    private var mediaRecorder: MediaRecorder? = null
    private var camera: Camera? = null

    private var isRecording = false

    private val NOTIFICATION_ID: Int = 1


    @RequiresApi(Build.VERSION_CODES.R)
    fun startForeground(filePath: String): Boolean {
        try {
            val notification = NotificationCompat.Builder(this, "ForegroundServiceChannelId")

                .setContentTitle("Foreground Service")
                .setContentText("Foreground service is running")
                .build()
            // Use ServiceCompat to start the foreground service with type:
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                } else {
                    0
                }
            );
          //  startRecording("")


            return true;
        } catch (e: Exception) {
            Log.e("ERROR", "ERROR: $e")
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

    fun stopService(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH);
            stopRecording()
            return true;
        } else {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE);
            stopRecording()
            return true;
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startRecording(filePath: String) {
        if (isRecording) return

        try {
            camera = Camera.open()
            val surfaceTexture = SurfaceTexture(10)
            camera?.setPreviewTexture(surfaceTexture)
            camera?.unlock()

            mediaRecorder = MediaRecorder().apply {
                setCamera(camera)
                setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                setVideoSource(MediaRecorder.VideoSource.CAMERA)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                setOutputFile(filePath)

                prepare()
                start()
            }

            isRecording = true
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun stopRecording() {
        if (!isRecording) return

        try {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        camera?.apply {
            stopPreview()
            lock()
            release()
        }

        mediaRecorder = null
        camera = null
        isRecording = false
    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}

