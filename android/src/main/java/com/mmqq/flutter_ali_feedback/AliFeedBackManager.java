package com.mmqq.flutter_ali_feedback;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.ErrorCode;
import com.alibaba.sdk.android.feedback.util.FeedbackErrorCallback;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;

import java.util.concurrent.Callable;

/**
 * Created by qq on 2020-3-10
 */
public class AliFeedBackManager {

    private static AliFeedBackManager mManager;
    private  Application mApplication;

    private AliFeedBackManager(Application application) {
        mApplication = application;
    }


    ///需要在AliFeedBackApplication中初始化获取Application对象
    public static AliFeedBackManager init(Application application) {
        if (mManager == null) {
            synchronized (AliFeedBackManager.class) {
                if (mManager == null) {
                    mManager = new AliFeedBackManager(application);
                }
            }
        }

        return mManager;
    }

    public static AliFeedBackManager getInstance(){
        if(mManager == null){
            throw new  Error("你必须先在AndroidManifest中引用AliFeedBackApplication,否则无法使用");
        }
        return mManager;
    }

    public  Context getContext(){
        return  mApplication.getApplicationContext();
    }

    public void init(String appkey,String appSecret) {
        /**
         * 添加自定义的error handler
         */
        FeedbackAPI.addErrorCallback(new FeedbackErrorCallback() {
            @Override
            public void onError(Context context, String errorMessage, ErrorCode code) {
                Toast.makeText(context, "ErrMsg is: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        FeedbackAPI.addLeaveCallback(new Callable() {
            @Override
            public Object call() throws Exception {
                Log.d("DemoApplication", "custom leave callback");
                return null;
            }
        });
        //默认初始化
        FeedbackAPI.init(mApplication, appkey, appSecret);
    }

    public void openFeedBack() {
        //设置默认联系方式
        FeedbackAPI.setDefaultUserContactInfo("");
        FeedbackAPI.setUserNick("");
        //沉浸式任务栏，控制台设置为true之后此方法才能生效
        FeedbackAPI.setTranslucent(false);
        //设置返回按钮图标
//        FeedbackAPI.setBackIcon(R.mipmap.action_bar_back_icon_blue);
        //设置标题栏"历史反馈"的字号，需要将控制台中此字号设置为0
        FeedbackAPI.setHistoryTextSize(14);
        //设置标题栏高度，单位为像素
        FeedbackAPI.setTitleBarHeight((int)dp2px(48));
        FeedbackAPI.openFeedbackActivity();
    }

    /***
     * 获取未读反馈数
     * @param callback
     */
    public void getUnReadFeedBackCount(IUnreadCountCallback callback) {
        FeedbackAPI.getFeedbackUnreadCount(callback);
    }

    private float dp2px(float dp) {
        final float scale = mApplication.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

}
