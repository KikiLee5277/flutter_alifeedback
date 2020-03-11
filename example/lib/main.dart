import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_ali_feedback/flutter_ali_feedback.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  bool feedbackStatus;

  @override
  void initState() {
    super.initState();
    initPlatformState();
    initFeedBack();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterAliFeedback.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> initFeedBack() async {
    bool success;
    String appKey = Platform.isIOS ?'28588453':'28502624';
    String appSecret = Platform.isIOS ?'dccf6241a45879256fc24149137b0ec3':'a92e28e1787aab06b34f9a6eaaa6da74';
    try {
      success = await FlutterAliFeedback.initFeedBack(appKey, appSecret);
    } on PlatformException {
      success = false;
    }
    if (!mounted) return;
    setState(() {
      feedbackStatus = success;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              Text('Running on: $_platformVersion\n'),
              Text('FeedBack Init: $feedbackStatus\n'),
              GestureDetector(
                onTap: () async{
                 bool success = await FlutterAliFeedback.openFeedBack(null);
                 print(success);
                },
                child: Text('打开意见反馈'),
              )
            ],
          ),
        ),
      ),
    );
  }
}
