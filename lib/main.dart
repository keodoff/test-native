import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

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
              final rez = await platform.invokeMethod('startForeground');
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
