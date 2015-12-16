# Password Dialog for Cordova

This is a [Cordova](http://cordova.apache.org/) plugin for showing password prompt dialogs.

There are currently two dialogs available:
* Confirm Password
* Change Password

# Install

To add the plugin to your Cordova project, simply add the plugin from the npm registry:

    cordova plugin add cordova-plugin-password-dialog

Alternatively, you can install the latest version of the plugin directly from git:

    cordova plugin add https://github.com/Justin-Credible/cordova-plugin-password-dialog

# Usage

The plugin is available via a global variable named `PasswordDialogPlugin`. It exposes the following properties and functions.

All functions accept optional success and failure callbacks as their final two arguments, where the failure callback will receive an error string as an argument unless otherwise noted.

A TypeScript definition file for the JavaScript interface is available in the `typings` directory as well as on [DefinitelyTyped](https://github.com/borisyankov/DefinitelyTyped) via the `tsd` tool.

## Confirm Password Dialog

Used to show a dialog that prompts the user to confirm their password. The user must enter their their password twice.

Method Signature:

`showConfirmPassword(options, successCallback, failureCallback)`

Options Parameter:

* title (string): The title for the dialog (optional, defaults to "Confirm Password").
* message (string): The message for the dialog (optional, defaults to empty string).
* minLength (number): The minimum length for the new password (optional, defaults to not enforcing a minimum length).

Example Usage:

    var options = {
        title: "Confirm Password",
        message: "Please confirm your password.",
        minLength: 8
    };

    PasswordDialogPlugin.showConfirmPassword(options,
        function(result) {
            if (result.cancel) {
                console.log("User cancelled the confirm password dialog.");
            }
            else {
                console.log("User completed the confirm password dialog.", result.password);
            }
        },
        function(err) {
            console.log("Confirm password dialog error.", err);
        });

## Change Password Dialog

Used to show a dialog that prompts the user to change their password. The user must enter their current password as well as their new password twice.

Method Signature:

`showChangePassword(options, successCallback, failureCallback)`

Options Parameter:

* title (string): The title for the dialog (optional, defaults to "Change Password").
* message (string): The message for the dialog (optional, defaults to empty string).
* minLength (number): The minimum length for the new password (optional, defaults to not enforcing a minimum length).

Example Usage:

    var options = {
        title: "Change Password",
        message: "Please change your password.",
        minLength: 8
    };

    PasswordDialogPlugin.showChangePassword(options,
        function(result) {
            if (result.cancel) {
                console.log("User cancelled the change password dialog.");
            }
            else {
                console.log("User completed the change password dialog.", result.currentPassword, result.newPassword);
            }
        },
        function(err) {
            console.log("Change password dialog error.", err);
        });
