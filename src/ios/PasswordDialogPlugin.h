//
//  PasswordDialogPlugin.h
//
//  Copyright (c) 2015 Justin Unterreiner. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>

@interface PasswordDialogPlugin : CDVPlugin
- (void)showChangePassword:(CDVInvokedUrlCommand *)command;
@end