import 'dart:developer';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:path_provider/path_provider.dart';
import 'package:test_foreground/permissions_provider.dart';

void main() {
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

Future<String> getFilePath() async {
  Directory storageDirectory = await getApplicationDocumentsDirectory();
  String sdPath = storageDirectory.path + "/record";
  var d = Directory(sdPath);
  if (!d.existsSync()) {
    d.createSync(recursive: true);
  }
  return sdPath + "/test.mp4";
}

class _HomePageState extends State<HomePage> {
  static const platform = MethodChannel('hello');
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
          child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          ElevatedButton(
            onPressed: () async {
              await PermissionsProvider.requestVideoPermissions();
              await PermissionsProvider.requestStoragePermissions();
            },
            child: Text('get permissions'),
          ),
          ElevatedButton(
            onPressed: () async {
              final path = await getFilePath();
              final rez = await platform.invokeMethod('startForeground', path);
              log('rez ${rez}');
            },
            child: Text('Start'),
          ),
          ElevatedButton(
            onPressed: () async {
              final rez = await platform.invokeMethod('stopForeground');
              log('rez ${rez}');
            },
            child: Text('Stop'),
          ),
        ],
      )),
    );
  }
}
