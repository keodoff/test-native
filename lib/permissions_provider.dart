import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

class PermissionsProvider {
  static Future<void> requestPermissions() async {
    await Permission.location.isDenied.then((bool isGranted) async {
      debugPrint('==DEBUG==: Requesting permission location.');
      final PermissionStatus status = await Permission.location.request();
      debugPrint('==DEBUG==: New status location: $status');
    });
  }

  static Future<bool> needToRequestAlwaysPermission() async {
    return await Permission.locationAlways.isGranted;
  }

  static Future<void> requestAlwaysPermissions() async {
    await Permission.locationAlways.isDenied.then((bool isGranted) async {
      debugPrint('==DEBUG==: Requesting permission locationAlways.');
      final PermissionStatus status = await Permission.locationAlways.request();
      debugPrint('==DEBUG==: New status locationAlways: $status');
    });
  }

  static Future<void> requestAudioPermissions() async {
    await Permission.audio.isDenied.then((bool isGranted) async {
      debugPrint('==DEBUG==: Requesting permission audio..');
      final PermissionStatus status = await Permission.audio.request();
      debugPrint('==DEBUG==: New status audio.: $status');
    });
  }

  static Future<void> requestMicroPermissions() async {
    await Permission.microphone.isDenied.then((bool isGranted) async {
      debugPrint('==DEBUG==: Requesting permission microphone.');
      final PermissionStatus status = await Permission.microphone.request();
      debugPrint('==DEBUG==: New status microphone: $status');
    });
  }

  static Future<void> requestStoragePermissions() async {
    await Permission.storage.isDenied.then((bool isGranted) async {
      debugPrint('==DEBUG==: Requesting permission storage.');
      final PermissionStatus status = await Permission.storage.request();
      debugPrint('==DEBUG==: New status storage: $status');
    });
  }

  static Future<void> requestVideoPermissions() async {
    await Permission.camera.isDenied.then((bool isGranted) async {
      debugPrint('==DEBUG==: Requesting permission storage.');
      final PermissionStatus status = await Permission.camera.request();
      debugPrint('==DEBUG==: New status storage: $status');
    });
  }

  static Future<void> requestAlarmPermissions() async {
    await Permission.scheduleExactAlarm.isDenied.then((bool isGranted) async {
      debugPrint('==DEBUG==: Requesting permission storage.');
      final PermissionStatus status =
          await Permission.scheduleExactAlarm.request();
      debugPrint('==DEBUG==: New status storage: $status');
    });
  }
}
