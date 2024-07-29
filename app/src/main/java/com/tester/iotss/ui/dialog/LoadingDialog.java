package com.tester.iotss.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import com.tester.iotss.R;

import java.util.Objects;


public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;


    public boolean isShowing = false;

    public LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(true);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowing = false;
            }
        });
        dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        isShowing = true;
    }

    public void dismissDialog(){
        dialog.dismiss();
        isShowing = false;
    }
}
