package com.carrus.carrusshipper.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


/**
 * Common Confirmation Dialog with callbacks for activity/Fragment
 */

public class CommonNoInternetDialog {

    private static AlertDialog mAlertDialog = null;

    /**
     * @param act The Activity reference
     * @return The value to be return
     */
    public static ConDialogActivity WithActivity(Activity act) {
        return new ConDialogActivity(act);
    }


    public interface ConfirmationDialogEventsListener {

        void OnOkButtonPressed();

        void OnCancelButtonPressed();

    }

    /**
     * Confirmation dialog class for activity
     */
    public static class ConDialogActivity {
        private final Activity activity;
        private ConfirmationDialogEventsListener confirmationDialogEvents;


        /**
         * @param activity The activity reference
         */
        public ConDialogActivity(Activity activity) {
            this.activity = activity;

        }


        /**
         * @param message The message
         * @param okButtonText The text of the Ok button
         * @param cancelButtonText The text of the cancel Button
         * @param confirmationDialogEvents1 The object of dialog listener
         */
        public void Show(String message, String okButtonText, String cancelButtonText, ConfirmationDialogEventsListener confirmationDialogEvents1) {
            try {
                this.confirmationDialogEvents = confirmationDialogEvents1;
                if (mAlertDialog == null)
                    mAlertDialog = new AlertDialog.Builder(activity)
                            // Set Dialog Icon
//                .setIcon(R.drawable.androidhappy)
                            // Set Dialog Title
                            .setTitle("")
                                    // Set Dialog Message
                            .setMessage(message)
                            .setCancelable(false)

                                    // Positive button
                            .setPositiveButton(okButtonText, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something else
                                    dialog.dismiss();
                                    mAlertDialog=null;
                                    confirmationDialogEvents.OnOkButtonPressed();
                                }
                            })
                            .setNegativeButton(cancelButtonText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    mAlertDialog=null;
                                    confirmationDialogEvents.OnCancelButtonPressed();
                                }
                            })
                            .create();
                mAlertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
