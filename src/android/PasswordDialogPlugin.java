package net.justin_credible.cordova;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class PasswordDialogPlugin extends CordovaPlugin {

    /**
     * A fake version of the R resource object.
     *
     * Used to obtain resources that were not compiled into the default R object.
     */
    private FakeR R;

    //region Base Overrides

    @Override
    public void pluginInitialize() {
        super.pluginInitialize();

        R = new FakeR(this.cordova.getActivity());
    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if (action == null) {
            return false;
        }

        if (action.equals("showChangePassword")) {

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        PasswordDialogPlugin.this.showChangePassword(args, callbackContext);
                    }
                    catch (Exception exception) {
                        callbackContext.error("PasswordDialogPlugin uncaught exception: " + exception.getMessage());
                    }
                }
            });

            return true;
        }
        else {
            // The given action was not handled above.
            return false;
        }
    }

    //endregion

    //region Cordova Commands

    private void showChangePassword(JSONArray args, final CallbackContext callbackContext) throws JSONException {

        // Ensure we have the correct number of arguments.
        if (args.length() != 3) {
            callbackContext.error("A title, message, and minLength are required.");
            return;
        }

        // Obtain the arguments.

        final String title = args.getString(0) == null ? "Change Password" : args.getString(0);
        final String message = args.getString(1) == null ? "" : args.getString(1);
        final int minLength = args.getInt(2);

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                PasswordDialogPlugin.this.showChangePasswordPrompt(title, message, minLength, callbackContext);
            }
        });
    }

    //region Helpers

    /**
     * Helper used to show the change password prompt dialog.
     *
     * @param title The title for the dialog.
     * @param message The message body for the dialog.
     * @param minLength The minimum length for the new password; -1 to not enforce.
     * @param callbackContext The Cordova plugin callback context.
     */
    private void showChangePasswordPrompt(String title, String message, final int minLength, final CallbackContext callbackContext) {

        // Create the builder for the dialog.
        Activity activity = PasswordDialogPlugin.this.cordova.getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        // Grab the dialog layout XML resource pointer.
        int dialogResource = R.getId("layout", "change_password_dialog");

        // Inflate the layout XML to get the layout object.
        LayoutInflater inflater = this.cordova.getActivity().getLayoutInflater();
        final LinearLayout dialogLayout = (LinearLayout) inflater.inflate(dialogResource, null);
        builder.setView(dialogLayout);

        // Configure the buttons and title/message.
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setTitle(title);
        builder.setMessage(message);

        // Create the dialog.
        final AlertDialog dialog = builder.create();

        // Obtain references to the input fields.

        EditText etCurrentPassword = (EditText) dialogLayout
                .findViewById(R.getId("id", "CurrentPassword"));

        EditText etNewPassword = (EditText) dialogLayout
                .findViewById(R.getId("id", "NewPassword"));

        EditText etConfirmPassword = (EditText) dialogLayout
                .findViewById(R.getId("id", "ConfirmPassword"));

        // Configure the type-face for each input.

        etCurrentPassword.setTypeface(Typeface.DEFAULT);
        etNewPassword.setTypeface(Typeface.DEFAULT);
        etConfirmPassword.setTypeface(Typeface.DEFAULT);

        // Wire up an event that will handle the "Done" or return key press on the last field.
        etConfirmPassword.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validateChangePassword(minLength, dialog, dialogLayout, callbackContext);
                    return true;
                }

                return false;
            }
        });

        // Open the dialog and focus the first field.
        dialog.show();
        etCurrentPassword.requestFocus();

        // Automatically show the keyboard for the first field.
        this.showKeyboardForField(this.cordova.getActivity(), etCurrentPassword);

        // Wire up the handlers for the buttons.

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validateChangePassword(minLength, dialog, dialogLayout, callbackContext);
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("cancel", true);

                callbackContext.success(new JSONObject(resultMap));
            }
        });
    }

    /**
     * Used to show the on-screen keyboard for the given text field as if the user
     * had tapped the field themselves.
     *
     * @param context Used to obtain a reference to the INPUT_METHOD_SERVICE.
     * @param textField The field we are showing the keyboard for.
     */
    public void showKeyboardForField(final Context context, final EditText textField) {

        textField.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.showSoftInput(textField, 0);
            }
        }, 200);
    }

    /**
     * Used to perform validation for the change password dialog.
     *
     * If validation passes, the dialog will be closed and the plugin callback will be invoked.
     *
     * If validation fails, validation messages will be shown on the appropriate text fields.
     *
     * @param minLength The minimum length for the new password; -1 to not enforce.
     * @param dialog The change password dialog instance.
     * @param dialogLayout The layout for the change password dialog.
     * @param callbackContext The Cordova plugin callback context.
     */
    private void validateChangePassword(int minLength, AlertDialog dialog, LinearLayout dialogLayout, CallbackContext callbackContext) {

        // Obtain references to the input fields.

        EditText etCurrentPassword = (EditText) dialogLayout
                .findViewById(R.getId("id", "CurrentPassword"));

        EditText etNewPassword = (EditText) dialogLayout
                .findViewById(R.getId("id", "NewPassword"));

        EditText etConfirmPassword = (EditText) dialogLayout
                .findViewById(R.getId("id", "ConfirmPassword"));

        // Grab the password values.

        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Perform validation.

        if (currentPassword.equals("")) {
            etCurrentPassword.setError("Current password is required.");
            etCurrentPassword.requestFocus();
            return;
        }

        if (newPassword.equals("")) {
            etNewPassword.setError("New password is required.");
            etNewPassword.requestFocus();
            return;
        }

        if (confirmPassword.equals("")) {
            etConfirmPassword.setError("Confirm new password is required.");
            etConfirmPassword.requestFocus();
            return;
        }

        if (minLength != -1 && newPassword.length() < minLength) {
            etNewPassword.setText("");
            etConfirmPassword.setText("");
            String message = String.format("The new password needs to be at least %d characters long.", minLength);
            etNewPassword.setError(message);
            etNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etNewPassword.setText("");
            etConfirmPassword.setText("");
            etNewPassword.setError("The new passwords do not match, please try again.");
            etNewPassword.requestFocus();
            return;
        }

        // If validation passed, invoke the plugin callback with the results.

        dialog.dismiss();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("cancel", false);
        resultMap.put("currentPassword", currentPassword);
        resultMap.put("newPassword", newPassword);

        callbackContext.success(new JSONObject(resultMap));
    }

    //endregion
}
