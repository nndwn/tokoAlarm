package com.tester.iotss.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tester.iotss.R;


public class AlertSuccess {

    Activity activity;
    AlertDialog dialog;

    boolean isShowing = false;

    public AlertSuccess(Activity myActivity){
        activity = myActivity;
    }

    public void startDialog(String title,String message){
        if(!isShowing) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            View mView = activity.getLayoutInflater().inflate(R.layout.alert_success, null);
            TextView tvTitle, tvMessage;
            Button btnTutup;
            tvTitle = (TextView) mView.findViewById(R.id.tvTitle);
            tvMessage = (TextView) mView.findViewById(R.id.tvMessage);
            btnTutup = (Button) mView.findViewById(R.id.btnTutup);
            btnTutup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShowing = false;
                    dialog.dismiss();
                }
            });

            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isShowing = false;
                }
            });

            tvTitle.setText(title);
            tvMessage.setText(message);
            builder.setView(mView);
            builder.setCancelable(true);

            dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            isShowing = true;
        }
    }

    public void dismissDialog(){
        isShowing = false;
        dialog.dismiss();
    }
}
