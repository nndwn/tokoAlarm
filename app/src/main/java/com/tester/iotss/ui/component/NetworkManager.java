package com.tester.iotss.ui.component;

import static com.tester.iotss.data.config.Config.BASE_URL;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tester.iotss.ui.dialog.AlertError;
import com.tester.iotss.ui.dialog.LoadingDialog;
import com.tester.iotss.utils.sessions.SessionLogin;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkManager {
    public static void renameDevice(Context context, String beforeRename, String afterRename, String idAlat, LoadingDialog loadingDialog, AlertError alertError, Runnable onRefresh) {
        SessionLogin sessionLogin = new SessionLogin();
        loadingDialog.startLoadingDialog();
        AndroidNetworking.post(BASE_URL + "users/rename")
                .setPriority(Priority.HIGH)
                .addBodyParameter("nohp", sessionLogin.getNohp(context))
                .addBodyParameter("id_alat", idAlat)
                .addBodyParameter("before_rename", beforeRename)
                .addBodyParameter("after_rename", afterRename)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingDialog.dismissDialog();
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                onRefresh.run();
                            } else {
                                alertError.startDialog("Gagal", response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            alertError.startDialog("Gagal", "Terjadi Kesalahan " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        loadingDialog.dismissDialog();
                        alertError.startDialog("Gagal", "Terjadi Kesalahan " + error.getMessage());
                    }
                });
    }
}
