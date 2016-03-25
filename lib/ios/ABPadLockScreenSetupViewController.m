//
//  PinEntryPlugin.m
//
//  Copyright (c) 2016 Justin Unterreiner. All rights reserved.
//

#import "PinEntryPlugin.h"
#import "ABPadLockScreenSetupViewController.h"

@implementation PinEntryPlugin

NSString *pinToVerify;

#pragma mark - Cordova Commands

- (void)showSetupPinPrompt:(CDVInvokedUrlCommand *)command {
    
    //    // Ensure we have the correct number of arguments.
    //    if ([command.arguments count] != 3) {
    //        CDVPluginResult *res = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"A title, message, and minLength are required."];
    //        [self.commandDelegate sendPluginResult:res callbackId:command.callbackId];
    //        return;
    //    }
    //
    //    // Obtain the arguments.
    //    NSString* title = [command.arguments objectAtIndex:0];
    //    NSString* message = [command.arguments objectAtIndex:1];
    //    int minLength = [command.arguments objectAtIndex:2] ?
    //    [[command.arguments objectAtIndex:2] intValue] : -1;
    //
    //    // Validate and/or default the arguments.
    //
    //    if (title == nil) {
    //        title = @"Confirm Password";
    //    }
    //
    //    if (message == nil) {
    //        message = @"";
    //    }
    
    ABPadLockScreenSetupViewController *pinScreen = [[ABPadLockScreenSetupViewController alloc]
                                                     initWithDelegate:self complexPin:NO subtitleLabelText:@"Please set a PIN."];

    pinScreen.modalPresentationStyle = UIModalPresentationFullScreen;
    pinScreen.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    
    pinScreen.tapSoundEnabled = YES;
    pinScreen.errorVibrateEnabled = YES;
    
    [pinScreen setLockScreenTitle:@"TITLE"];//no (same as setEnterPasscodeLabelText, but also sets the title propery)
    [pinScreen setSubtitleText:@"SUB-TITLE"];//no
    [pinScreen setCancelButtonText:@"CANCEL"];//ok
    [pinScreen setDeleteButtonText:@"DELETE"];//ok
//    [pinScreen setEnterPasscodeLabelText:@"ENTER PASSCODE"];//ok
    [pinScreen cancelButtonDisabled:NO];//ok

    [[pinScreen view] setAlpha:0.5];
    
    // setup specific
    pinScreen.pinConfirmationText = @"PIN CONFIRM TEXT";
    pinScreen.pinNotMatchedText = @"PIN NOT MATCHED TEXT";
    
    [self presentViewController:pinScreen];
}

- (void)showPinPrompt:(CDVInvokedUrlCommand *)command {
    
//    // Ensure we have the correct number of arguments.
//    if ([command.arguments count] != 3) {
//        CDVPluginResult *res = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"A title, message, and minLength are required."];
//        [self.commandDelegate sendPluginResult:res callbackId:command.callbackId];
//        return;
//    }
//    
//    // Obtain the arguments.
//    NSString* title = [command.arguments objectAtIndex:0];
//    NSString* message = [command.arguments objectAtIndex:1];
//    int minLength = [command.arguments objectAtIndex:2] ?
//    [[command.arguments objectAtIndex:2] intValue] : -1;
//    
//    // Validate and/or default the arguments.
//    
//    if (title == nil) {
//        title = @"Confirm Password";
//    }
//    
//    if (message == nil) {
//        message = @"";
//    }
    
    ABPadLockScreenViewController *pinScreen = [[ABPadLockScreenViewController alloc] initWithDelegate:self complexPin:NO];

    pinScreen.modalPresentationStyle = UIModalPresentationFullScreen;
    pinScreen.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    
    pinScreen.tapSoundEnabled = YES;
    pinScreen.errorVibrateEnabled = YES;
    
    [pinScreen setLockScreenTitle:@"TITLE"];//no (same as setEnterPasscodeLabelText, but also sets the title propery)
    [pinScreen setSubtitleText:@"SUB-TITLE"];//ok
    [pinScreen setCancelButtonText:@"CANCEL"];//ok
    [pinScreen setDeleteButtonText:@"DELETE"];//ok
//    [pinScreen setEnterPasscodeLabelText:@"ENTER PASSCODE"];//ok
    [pinScreen cancelButtonDisabled:YES];//ok

    [[pinScreen view] setAlpha:0.5];

    // enter specific
    [pinScreen setAllowedAttempts:3];//ok
    [pinScreen setLockedOutText:@"LOCKED OUT"];//ok
    [pinScreen setPluralAttemptsLeftText:@"ATTEMPTS"];//ok
    [pinScreen setSingleAttemptLeftText:@"ATTEMPT"];//ok
    
    [self presentViewController:pinScreen];
}

#pragma mark - ABLockScreenDelegate Methods

- (BOOL)padLockScreenViewController:(ABPadLockScreenViewController *)padLockScreenViewController validatePin:(NSString*)pin;
{
    NSLog(@"Validating pin %@", pin);
    
    return [pinToVerify isEqualToString:pin];
}

- (void)unlockWasSuccessfulForPadLockScreenViewController:(ABPadLockScreenViewController *)padLockScreenViewController
{
//    [self dismissViewControllerAnimated:YES completion:nil];
    NSLog(@"Pin entry successfull");
}

- (void)unlockWasUnsuccessful:(NSString *)falsePin afterAttemptNumber:(NSInteger)attemptNumber padLockScreenViewController:(ABPadLockScreenViewController *)padLockScreenViewController
{
    NSLog(@"Failed attempt number %ld with pin: %@", (long)attemptNumber, falsePin);
}

- (void)unlockWasCancelledForPadLockScreenViewController:(ABPadLockScreenAbstractViewController *)padLockScreenViewController
{
//    [self dismissViewControllerAnimated:YES completion:nil];
    NSLog(@"Pin entry cancelled");
}

#pragma mark - ABPadLockScreenSetupViewControllerDelegate Methods
- (void)pinSet:(NSString *)pin padLockScreenSetupViewController:(ABPadLockScreenSetupViewController *)padLockScreenViewController
{
//    [self dismissViewControllerAnimated:YES completion:nil];
    //self.thePin = pin;
    NSLog(@"Pin set to pin %@", pin);
}

- (void)unlockWasCancelledForSetupViewController:(ABPadLockScreenAbstractViewController *)padLockScreenViewController
{
//    [self dismissViewControllerAnimated:YES completion:nil];
    NSLog(@"Pin Setup Cnaclled");
}

#pragma mark - Shared Helper Methods

/**
 * Helper used to ensure the given alert controller is presented on the active view controller.
 */
- (void)presentViewController:(UIViewController*)viewController {
    
    // Grab the view controller that is currently presented.
    UIViewController *currentViewController = [[[UIApplication sharedApplication] delegate] window].rootViewController;

    // Now present the alert on the view controller that is currently presenting.
    if (currentViewController) {
        
        // Note that since Cordova's view controller may not be the one that is currently
        // presented (eg if another plugin that uses native controllers such as the InAppBrowser
        // is currently presenting) we have to do some extra checking. So here we dig down
        // and find the current view controller.
        while (currentViewController.presentedViewController){
            currentViewController = currentViewController.presentedViewController;
        }
        
        [currentViewController presentViewController:viewController animated:YES completion:nil];
    }
    else {
        // Fallback and present on Cordova's view controller.
        [self.viewController presentViewController:viewController animated:YES completion:nil];
    }
}

@end
