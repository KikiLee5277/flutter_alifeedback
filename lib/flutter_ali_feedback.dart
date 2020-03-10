import 'dart:async';

import 'package:flutter/services.dart';

class FlutterAliFeedback {
  static const MethodChannel _channel =
      const MethodChannel('flutter_ali_feedback');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> initFeedBack(String appKey, String appSecret) async {
    Map map = {'AppKey': appKey, 'AppSecret': appSecret};
    final bool success = await _channel.invokeMethod('initFeedBack', map);
    return success;
  }

  ///FlutterAliFeedBackConfig 配置反馈的一些信息
  static Future<bool> openFeedBack(FlutterAliFeedBackConfig config) async {
    if(config == null){
      config = FlutterAliFeedBackConfig();
    }
    Map map = {
      'contact':config.contact,
      'userName':config.userName,
      'isTranslucent':config.isTranslucent,
      'historyTextSize':config.historyTextSize,
      'titleBarHeight':config.titleBarHeight,
      'isLogEnable':config.isLogEnable,
      'extInfo':config.extInfo,
      'setBackIcon':config.setBackIcon,
      'webViewUrl':config.webViewUrl,
    };
    final bool success = await _channel.invokeMethod('openFeedBack',map);
    return success;
  }

  static Future<dynamic> get getUnReadFeedBackCount async {
    final result = await _channel.invokeMethod('getUnReadFeedBackCount');
    return result;
  }
}

class FlutterAliFeedBackConfig {
  final String contact; //联系人
  final String userName; //用户名
  final bool isTranslucent; //是否透明
  final num historyTextSize; //历史反馈的字体大小
  final num titleBarHeight; //actionBar高度
  final bool isLogEnable; //是否开启日志
  final Map<String, dynamic> extInfo; //设置反馈消息自定义参数
  final int setBackIcon; //设置返回按钮的图片
  final String webViewUrl;

  FlutterAliFeedBackConfig(
      {this.contact = '',
      this.userName = '',
      this.isTranslucent = true,
      this.historyTextSize = 14,
      this.titleBarHeight = 48,
      this.isLogEnable = false,
      this.extInfo,
      this.setBackIcon=0,
      this.webViewUrl});
}
