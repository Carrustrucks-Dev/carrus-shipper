package com.carrus.carrusshipper.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.carrus.carrusshipper.R;


/**
 * Common Confirmation Dialog with callbacks for activity/Fragment
 */

public class CommonConfirmationDialog {

    /**
     * @param act
     * @return
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
        private Activity activity;
        private ConfirmationDialogEventsListener confirmationDialogEvents;


        /**
         * @param activity
         */
        public ConDialogActivity(Activity activity) {
            this.activity = activity;

        }


        /**
         * @param message
         * @param okButtonText
         * @param cancelButtonText
         * @param confirmationDialogEvents1
         */
        public void Show(String message, String okButtonText, String cancelButtonText, ConfirmationDialogEventsListener confirmationDialogEvents1) {
            try {
                this.confirmationDialogEvents = confirmationDialogEvents1;
                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.dialog_confirmation);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView textHead = (TextView) dialog.findViewById(R.id.textHead);

                textHead.setVisibility(View.GONE);
                TextView textMessage = (TextView) dialog
                        .findViewById(R.id.textMessage);
                textMessage.setText(message);

                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

                if (!okButtonText.isEmpty())
                    btnOk.setText(okButtonText);

                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                if (!cancelButtonText.isEmpty())
                    btnCancel.setText(cancelButtonText);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        confirmationDialogEvents.OnOkButtonPressed();
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        confirmationDialogEvents.OnCancelButtonPressed();
                    }

                });

                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * @param message
         * @param confirmationDialogEvents1
         */
        public void Show(String message, ConfirmationDialogEventsListener confirmationDialogEvents1) {
            try {

                this.confirmationDialogEvents = confirmationDialogEvents1;
                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.dialog_confirmation);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView textHead = (TextView) dialog.findViewById(R.id.textHead);

                textHead.setVisibility(View.GONE);
                TextView textMessage = (TextView) dialog
                        .findViewById(R.id.textMessage);
                textMessage.setText(message);

                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);


                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        confirmationDialogEvents.OnOkButtonPressed();
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        confirmationDialogEvents.OnCancelButtonPressed();
                    }

                });

                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
