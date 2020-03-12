# flutter_ali_feedback

flutter版本的阿里反馈sdk集成

Android接入需要在主工程的AndroidManifest中引入com.mmqq.flutter_ali_feedback.AliFeedBackApplication类

iOS端：需要接入UTDID库时，可指定master分支；不需要UTDID库时，可指定ios-no-utdid分支(手动集成阿里反馈SDK_3.3.6)。
master分支通过cocoapods集成阿里反馈，需要在 Podfile 文件中添加如下内容：
# 指定 Master 仓库和阿里云仓库
source 'https://github.com/CocoaPods/Specs.git'
source 'https://github.com/aliyun/aliyun-specs.git'

flutter中使用方法:
在首页initState方法中注册

 Future<void> initFeedBack() async {
    bool  success = await FlutterAliFeedback.initFeedBack('appKey', 'appSecret');
 }
 
在需要的地方调用打开反馈页面:

() async{
    bool success = await FlutterAliFeedback.openFeedBack(null);
 }

获取反馈未读消息数:

() async{
    var unReadCount = await FlutterAliFeedback.getUnReadFeedBackCount;
 }
 

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter, view our 
[online documentation](https://flutter.dev/docs), which offers tutorials, 
samples, guidance on mobile development, and a full API reference.
