// Type definitions for cordova-plugin-password-dialog 1.1.0
// Project: https://github.com/Justin-Credible/cordova-plugin-password-dialog
// Definitions by: Justin Unterreiner <https://github.com/Justin-Credible>
// Definitions: https://github.com/borisyankov/DefinitelyTyped

declare module PasswordDialogPlugin {

    interface PasswordDialogPluginStatic {

        /**
         * Used to show a dialog that prompts the user to confirm their password.
         * The user must enter their current password as well as their new password twice.
         * 
         * @param options The options for the dialog.
         * @param successCallback The success callback for this asynchronous function; receives a result object.
         * @param failureCallback The failure callback for this asynchronous function; receives an error string.
         */
        showConfirmPassword(options: ConfirmPasswordOptions, successCallback?: (result: ConfirmPasswordResult) => void, failureCallback?: (error: string) => void): void;

        /**
         * Used to show a dialog that prompts the user to change their password.
         * The user must enter their current password as well as their new password twice.
         * 
         * @param options The options for the dialog.
         * @param successCallback The success callback for this asynchronous function; receives a result object.
         * @param failureCallback The failure callback for this asynchronous function; receives an error string.
         */
        showChangePassword(options: ChangePasswordOptions, successCallback?: (result: ChangePasswordResult) => void, failureCallback?: (error: string) => void): void;
    }

    /**
     * Options for use with showConfirmPassword().
     */
    interface ConfirmPasswordOptions {

        /**
         * The title for the dialog.
         * 
         * Optional, defaults to "Confirm Password" if not provided.
         */
        title?: string;

        /**
         * The message for the dialog.
         * 
         * Optional, defaults to empty string if not provided.
         */
        message?: string;

        /**
         * The minimum length for the new password.
         * 
         * Optional, defaults to not enforcing a minimum length if not provided.
         */
        minLength?: number;
    }

    /**
     * Result for the success callback to showConfirmPassword()
     */
    interface ConfirmPasswordResult {

        /**
         * Indicates if the user cancelled the dialog or not.
         */
        cancel: boolean;

        /**
         * The user's password.
         */
        password: string;
    }

    /**
     * Options for use with showChangePassword().
     */
    interface ChangePasswordOptions {

        /**
         * The title for the dialog.
         * 
         * Optional, defaults to "Change Password" if not provided.
         */
        title?: string;

        /**
         * The message for the dialog.
         * 
         * Optional, defaults to empty string if not provided.
         */
        message?: string;

        /**
         * The minimum length for the new password.
         * 
         * Optional, defaults to not enforcing a minimum length if not provided.
         */
        minLength?: number;
    }

    /**
     * Result for the success callback to showChangePassword()
     */
    interface ChangePasswordResult {

        /**
         * Indicates if the user cancelled the dialog or not.
         */
        cancel: boolean;

        /**
         * The user's current password.
         */
        currentPassword: string;

        /**
         * The user's new password.
         */
        newPassword: string;
    }
}

declare var PasswordDialogPlugin: PasswordDialogPlugin.PasswordDialogPluginStatic;
