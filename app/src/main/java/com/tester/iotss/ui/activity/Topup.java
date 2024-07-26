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
import android.view.View;
import android.widget.LinearLayout;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topup);
        ButterKnife.bind(this);


        LinearLayout btnSepuluh = findViewById(R.id.Sepuluh);
        LinearLayout btnTigaPuluh = findViewById(R.id.tigaPuluh);
        LinearLayout btnLimaPuluh = findViewById(R.id.LimaPuluh);
        LinearLayout btnSeratus = findViewById(R.id.Seratus);
        LinearLayout btnSatuJuta = findViewById(R.id.SatuJuta);

        btnSepuluh.setOnClickListener(v -> openDisplayValueActivity("Rp. 10"));

        btnTigaPuluh.setOnClickListener(v -> openDisplayValueActivity("Rp. 30"));

        btnLimaPuluh.setOnClickListener(v -> openDisplayValueActivity("Rp. 50"));

        btnSeratus.setOnClickListener(v -> openDisplayValueActivity("Rp. 100"));

        btnSatuJuta.setOnClickListener(v -> openDisplayValueActivity("Rp. 1.000"));
    }
    private void openDisplayValueActivity(String value) {
        Intent intent = new Intent(Topup.this, TopupValue.class);
        intent.putExtra("value", value);
        startActivity(intent);
    }

    @OnClick(R.id.backButton)
    void backButton() {
        onBackPressed();
    }


}