package com.mmqq.flutter_ali_feedback;

import android.text.TextUtils;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;

import org.json.JSONObject;

import java.util.Map;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterAliFeedbackPlugin
 */
public class FlutterAliFeedbackPlugin implements FlutterPlugin, MethodCallHandler {


    public FlutterAliFeedbackPlugin() {
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_ali_feedback");
        channel.setMethodCallHandler(new FlutterAliFeedbackPlugin());
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_ali_feedback");
        channel.setMethodCallHandler(new FlutterAliFeedbackPlugin());
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("initFeedBack")) {
            Map<String,String> params = call.arguments();
            String appkey = params.get("AppKey");
            String appSecret = params.get("AppSecret");
            if (TextUtils.isEmpty(appkey)) {
                result.error("-1", "AliFeedBack AppKey is Null", "");
            } else if (TextUtils.isEmpty(appSecret)) {
                result.error("-1", "AliFeedBack AppSecret is Null", "");
            } else {
                AliFeedBackManager.getInstance().init(appkey, appSecret);
                result.success(true);
            }
        } else if (call.method.equals("openFeedBack")) {
            try {
                Map<String,Object> params = call.arguments();
                String contact = (String) params.get("contact");
                String userName = (String) params.get("userName");
                boolean isTranslucent = (boolean) params.get("isTranslucent");
                float historyTextSize = (float) params.get("historyTextSize");
                float titleBarHeight = (float) params.get("titleBarHeight");
                boolean isLogEnable = (boolean) params.get("isLogEnable");
                Map<String, Object> extInfo = (Map<String, Object>) params.get("extInfo");
                int setBackIcon = (int) params.get("setBackIcon");
                String webViewUrl = (String) params.get("webViewUrl");
                FeedbackAPI.setDefaultUserContactInfo(contact);
                FeedbackAPI.setUserNick(userName);
                FeedbackAPI.setTranslucent(isTranslucent);
                FeedbackAPI.setHistoryTextSize(historyTextSize);
                FeedbackAPI.setTitleBarHeight((int) titleBarHeight);
                FeedbackAPI.setLogEnabled(isLogEnable);
                if(extInfo !=null && extInfo.size() >0){
                    JSONObject object = new JSONObject();
                   for(String key : extInfo.keySet()){
                       object.putOpt(key,extInfo.get(key));
                   }
                    FeedbackAPI.setAppExtInfo(object);
                }
                FeedbackAPI.setBackIcon(setBackIcon);
                FeedbackAPI.setWebViewUrl(webViewUrl);
            }catch (Exception e){
                result.error("-1","openFeedBack params parse error","");
            }
            FeedbackAPI.openFeedbackActivity();
            result.success(true);
        } else if (call.method.equals("getUnReadFeedBackCount")) {
            AliFeedBackManager.getInstance().getUnReadFeedBackCount(new IUnreadCountCallback() {
                @Override
                public void onSuccess(int i) {
                    result.success(i);
                }

                @Override
                public void onError(int i, String s) {
                    result.error(i + "", s, s);
                }
            });
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    }
}
