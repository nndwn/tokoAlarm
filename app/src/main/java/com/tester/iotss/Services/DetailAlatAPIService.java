package com.tester.iotss.Services;

import static com.tester.iotss.Configs.Config.BASE_URL;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tester.iotss.Monitoring;
import com.tester.iotss.R;
import com.tester.iotss.Session.SessionLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailAlatAPIService {
    Context context;

    public DetailAlatAPIService(Context ctx) {
        this.context = ctx;
    }

    public void getSensorName(String id_alat, String nomor_paket, SensorNameCallback callback) {
        SessionLogin sessionLogin = new SessionLogin();
        String[] daftarNamaSensor = new String[3];
        AndroidNetworking.post(BASE_URL + "users/bookalatdetail")
                .setPriority(Priority.HIGH)
                .addBodyParameter("nohp", sessionLogin.getNohp(this.context))
                .addBodyParameter("id_alat", id_alat)
                .addBodyParameter("nomor_paket", nomor_paket)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        try {
                            boolean status = person.getBoolean("status");
                            if (status) {
                                if (person.getJSONObject("renamed").getBoolean("status")) {
                                    JSONArray renamedArray = person.getJSONObject("renamed").getJSONArray("data");
                                    for (int j = 0; j < renamedArray.length(); j++) {
                                        JSONObject renamedObject = renamedArray.getJSONObject(j);
                                        String beforeRename = renamedObject.getString("before_rename");
                                        String afterRename = renamedObject.getString("after_rename");

                                        switch (beforeRename) {
                                            case "in1":
                                                daftarNamaSensor[0] = afterRename;
                                                Log.d("DATAJSON", afterRename);
                                                break;
                                            case "in2":
                                                daftarNamaSensor[1] = afterRename;
                                                break;
                                            case "in3":
                                                daftarNamaSensor[2] = afterRename;
                                                break;
                                        }
                                    }
                                    callback.onSensorNameReceived(daftarNamaSensor);
                                }
                            } else {
                                callback.onError(person.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("FRAGMENTHOMELOG", e.getMessage());
                            callback.onError("Terjadi Kesalahan " + e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("DETAILALAT", error.getMessage());
                        callback.onError(error.getMessage());
                    }
                });
    }

    public interface SensorNameCallback {
        void onSensorNameReceived(String[] sensorNames);

        void onError(String errorMessage);
    }

}
