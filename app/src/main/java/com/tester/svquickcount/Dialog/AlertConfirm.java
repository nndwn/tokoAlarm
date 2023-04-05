package com.tester.svquickcount.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tester.svquickcount.R;


public class AlertConfirm {
    Activity activity;
    AlertDialog dialog;
    ConfirmCallback confirmCallback;


    public interface ConfirmCallback {
        public void onConfirmYa();
        public void onConfirmNo();
    }
    public AlertConfirm(Activity myActivity, ConfirmCallback confirmCallback){
        activity = myActivity;
        this.confirmCallback = confirmCallback;
    }

    public void startDialog(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View mView=activity.getLayoutInflater().inflate(R.layout.alert_confirm,null);
        TextView tvTitle,tvMessage;
        Button btnNo,btnYa;
        tvTitle = (TextView) mView.findViewById(R.id.tvTitle);
        tvMessage=(TextView) mView.findViewById(R.id.tvMessage);
        btnNo = (Button) mView.findViewById(R.id.btnNo);
        btnYa = (Button) mView.findViewById(R.id.btnYa);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCallback.onConfirmNo();
            }
        });

        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCallback.onConfirmYa();
            }
        });
        tvTitle.setText(title);
        tvMessage.setText(message);
        builder.setView(mView);
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
