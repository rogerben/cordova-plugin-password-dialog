"use strict";

var exec = require("cordova/exec");

/**
 * The Cordova plugin ID for this plugin.
 */
var PLUGIN_ID = "PasswordDialogPlugin";

/**
 * The plugin which will be exported and exposed in the global scope.
 */
var PasswordDialogPlugin = {};

/**
 * Used to show a dialog that prompts the user to change their password.
 * The user must enter their current password as well as their new password twice.
 * 
 * The options available are:
 *  • [string] title - The title for the dialog (optional, defaults to "Change Password").
 *  • [string] message - The message for the dialog (optional, defaults to empty string).
 *  • [number] minLength - The minimum length for the new password (optional, defaults to not enforcing a minimum length).
 * 
 * Upon completion, the success callback will be invoked with the following object:
 * 
 *  { cancel: false, currentPassword = "oldpass", newPassword = "newpass" }
 * 
 * @param [object] options - The options for the dialog.
 * @param [function] successCallback - The success callback for this asynchronous function; receives a result object.
 * @param [function] failureCallback - The failure callback for this asynchronous function; receives an error string.
 */
PasswordDialogPlugin.showChangePassword = function showChangePassword(options, successCallback, failureCallback) {

    if (typeof(options) !== "object") {
        options = {};
    }

    if (typeof(options.title) !== "string") {
        options.title = "Change Password";
    }

    if (typeof(options.message) !== "string") {
        options.message = "";
    }

    if (typeof(options.minLength) !== "number") {
        options.minLength = -1;
    }

    exec(successCallback, failureCallback, PLUGIN_ID, "showChangePassword", [options.title, options.message, options.minLength]);
};

module.exports = PasswordDialogPlugin;
