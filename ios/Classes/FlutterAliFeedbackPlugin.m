#import "FlutterAliFeedbackPlugin.h"
#import <YWFeedbackFMWK/YWFeedbackKit.h>
#import <YWFeedbackFMWK/YWFeedbackViewController.h>

@interface FlutterAliFeedbackPlugin ()

@property (nonatomic, strong) YWFeedbackKit *feedbackKit;

@end

@implementation FlutterAliFeedbackPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"flutter_ali_feedback"
            binaryMessenger:[registrar messenger]];
  FlutterAliFeedbackPlugin* instance = [[FlutterAliFeedbackPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
      result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else if ([@"initFeedBack" isEqualToString:call.method]) {
      result(@([self feedbackInitWithAppKey:call.arguments[@"AppKey"] appSecret:call.arguments[@"AppSecret"]]));
  } else if ([@"openFeedBack" isEqualToString:call.method]) {
      result(@([self openFeedbackViewControllerWithExtInfo:call.arguments]));
  } else if ([@"getUnReadFeedBackCount" isEqualToString:call.method]) {
      result([self getUnReadFeedBackCount]);
  } else {
      result(FlutterMethodNotImplemented);
  }
}

- (BOOL)feedbackInitWithAppKey:(NSString *)appKey appSecret:(NSString *)appSecret {
    if (appKey == nil ||
        [appKey isKindOfClass:[NSString class]] == NO ||
        appSecret == nil ||
        [appSecret isKindOfClass:[NSString class]] == NO) {
        return NO;
    }
    self.feedbackKit = [[YWFeedbackKit alloc] initWithAppKey:appKey
                                                   appSecret:appSecret];
    return YES;
}

- (BOOL)openFeedbackViewControllerWithExtInfo:(NSDictionary *)extInfo {
    if (self.feedbackKit == nil) {
        return NO;
    }
    if (extInfo && [extInfo isKindOfClass:[NSDictionary class]]) {
        self.feedbackKit.extInfo = extInfo;
    }
    __block BOOL result = NO;
    [self.feedbackKit makeFeedbackViewControllerWithCompletionBlock:^(BCFeedbackViewController *viewController, NSError *error) {
        if (viewController != nil) {
            [viewController setCloseBlock:^(UIViewController *aParentController){
                [aParentController dismissViewControllerAnimated:YES completion:nil];
            }];

            UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:viewController];
            UIViewController *rootVC = [UIApplication sharedApplication].delegate.window.rootViewController;
            [rootVC presentViewController:nav animated:YES completion:^{

            }];

            result = YES;
        } else {
            result = NO;
        }
    }];
    return result;
}

- (NSNumber *)getUnReadFeedBackCount {
    if (self.feedbackKit == nil) {
        return @0;
    } else {
        __block NSNumber *result = @0;
        [self.feedbackKit getUnreadCountWithCompletionBlock:^(NSInteger unreadCount, NSError *error) {
            if (error == nil) {
                result = @(unreadCount);
            }
        }];
        return result;
    }
}

@end
