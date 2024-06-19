package com.tester.iotss.ui.activity;

import static com.tester.iotss.data.config.Config.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tester.iotss.R;
import com.tester.iotss.utils.helpers.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Topup extends AppCompatActivity {
    private Context context;
    String nomor_cs = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_topup);
        ButterKnife.bind(this);
        getData();
    }

    @OnClick(R.id.backButton)
    void backButton() {
        onBackPressed();
    }

    private void getData() {
        AndroidNetworking.get(BASE_URL + "users/pusatbantuan")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        Log.d("PUSATBANTUANLOG", person.toString());
                        try {

                            boolean status = person.getBoolean("status");
                            if (status) {
                                JSONObject data = person.getJSONObject("data");
                                nomor_cs = data.getString("nomor_cs");
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("PUSATBANTUANLOG", e.getMessage());
                            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("PUSATBANTUANLOG", error.getMessage());
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.lnChatCs)
    void lnChatCs() {
        String countryCode = "62";
        String formattedNumber = nomor_cs.replaceFirst("^0", countryCode);
        AppHelper.openWhatsApp(context, formattedNumber, "Saya ingin melakukan Topup Saldo Tokoalarm");
    }


}