package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SuccessRegistrasi extends AppCompatActivity {


    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvHp)
    TextView tvHp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_registrasi);
        ButterKnife.bind(this);
        tvNama.setText(getIntent().getStringExtra("nama"));
        tvEmail.setText(getIntent().getStringExtra("email"));
        tvHp.setText(getIntent().getStringExtra("hp"));
    }


    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SuccessRegistrasi.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
        finish();
    }

    @OnClick(R.id.btnLogin) void btnLogin(){
        Intent intent = new Intent(SuccessRegistrasi.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
        finish();
    }
}
