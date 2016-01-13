package com.carrus.carrusshipper.utils;

import android.app.Activity;
import android.app.Dialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrus.carrusshipper.R;

public class ImageChooserDialog {


    private Activity activity;
    private OnButtonClicked onButtonClicked;

    /**
     * @param act
     */
    private ImageChooserDialog(Activity act) {
        activity = act;
    }

    /**
     * @param activity
     * @return
     */
    public static ImageChooserDialog With(Activity activity) {
        return new ImageChooserDialog(activity);
    }

    /**
     * @param message
     */
    public void Show(String message, OnButtonClicked onOkButtonClicked1) {
        try {

            onButtonClicked = onOkButtonClicked1;
            final Dialog dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_image_chooser);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.textMessage);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnGalery = (Button) dialog.findViewById(R.id.btnGalery);

            btnGalery.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    onButtonClicked.onGaleryClicked();
                    dialog.dismiss();
                }

            });

            Button btnCamera = (Button) dialog.findViewById(R.id.btnCamera);

            btnCamera.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    onButtonClicked.onCameraClicked();
                    dialog.dismiss();
                }

            });

            ImageView crossBtn=(ImageView) dialog.findViewById(R.id.crossBtn);
            crossBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnButtonClicked {
        void onGaleryClicked();
        void onCameraClicked();
    }
}
