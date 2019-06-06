//
//  ejwork.tx.gps.h
//  HelloWorld
//
//
//

#import <Cordova/CDV.h>
#import <CoreLocation/CoreLocation.h>

#define IS_OS_8_OR_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)

@interface ejwork.tx.gps : CDVPlugin <CLLocationManagerDelegate>

- (void)getLocation:(CDVInvokedUrlCommand*)command;
- (void)stopLocation:(CDVInvokedUrlCommand*)command;
@end
