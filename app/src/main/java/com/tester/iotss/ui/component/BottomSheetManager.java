package com.tester.iotss.ui.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tester.iotss.R;

public class BottomSheetManager {
    public static void showRenameBottomSheet(Context ctx, String currentName, BottomSheetDialog sheetDialog) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.sheet_rename, null);
        EditText edRename = view.findViewById(R.id.edRename);
        Button btnSimpan = view.findViewById(R.id.btnSimpan);

            edRename.setText(currentName);

        btnSimpan.setOnClickListener(view1 -> {
            sheetDialog.dismiss();
        });

        sheetDialog.setContentView(view);
        sheetDialog.show();
    }

    public static void showDelayBottomSheet(Context context, String beforeDelay, BottomSheetDialog dialog, Runnable onDelay) {
        View view = LayoutInflater.from(context).inflate(R.layout.sheet_alarm, null);
        EditText edDelay = view.findViewById(R.id.edDelay);
        Button btnSimpan = view.findViewById(R.id.btnSimpan);

        edDelay.setText(beforeDelay);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onDelay.run();
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }
}

