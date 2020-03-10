package com.mmqq.flutter_ali_feedback;

import io.flutter.app.FlutterApplication;

/**
 * 因为阿里反馈需要获取application对象来初始化sdk,所以需要重写FlutterApplication
 * Created by qq on 2020-03-10
 */
public class AliFeedBackApplication extends FlutterApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AliFeedBackManager.init(this);
    }
}
