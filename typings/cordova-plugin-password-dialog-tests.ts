/// <reference path="cordova-plugin-password-dialog.d.ts" />

var emptyOptions: PasswordDialogPlugin.ChangePasswordOptions = {};

var options: PasswordDialogPlugin.ChangePasswordOptions = {
    title: "title",
    message: "message",
    minLength: 8
};

PasswordDialogPlugin.showChangePassword(null);
PasswordDialogPlugin.showChangePassword(options);
PasswordDialogPlugin.showChangePassword(options, () => {});
PasswordDialogPlugin.showChangePassword(options, () => {}, (error: string) => {});
PasswordDialogPlugin.showChangePassword(options, (result: PasswordDialogPlugin.ChangePasswordResult) => {}, (error: string) => {});
