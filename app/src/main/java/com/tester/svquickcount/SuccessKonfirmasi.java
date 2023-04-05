package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SuccessKonfirmasi extends AppCompatActivity {


    String grandtotal,invoice,total_bayar,total_item,potongan,message;
    @BindView(R.id.tvInvoice)
    TextView tvInvoice;
    @BindView(R.id.tvTotalItem)
    TextView tvTotalItem;
    @BindView(R.id.tvTotalBayar)
    TextView tvTotalBayar;
    @BindView(R.id.tvPotongan)
    TextView tvPotongan;
    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_konfirmasi);
        ButterKnife.bind(this);
        if(getIntent().hasExtra("grandtotal")){
            grandtotal = getIntent().getStringExtra("grandtotal");
            invoice = getIntent().getStringExtra("invoice");
            total_bayar= getIntent().getStringExtra("total_bayar");
            total_item = getIntent().getStringExtra("total_item");
            potongan = getIntent().getStringExtra("potongan");
            message = getIntent().getStringExtra("message");
            tvMessage.setText(message);
            String strpotongan = new DecimalFormat("##,##0").format(Long.parseLong(getIntent().getStringExtra("potongan")));
            String strtotalbayar = new DecimalFormat("##,##0").format(Long.parseLong(getIntent().getStringExtra("total_bayar")));
            String strgrandtotal = new DecimalFormat("##,##0").format(Long.parseLong(getIntent().getStringExtra("grandtotal")));
            tvInvoice.setText("#"+invoice);
            tvTotalItem.setText(total_item);
            tvPotongan.setText("Rp "+strpotongan);
            tvTotalBayar.setText("Rp "+strtotalbayar);
            tvGrandTotal.setText("Rp "+strgrandtotal);
        }
    }


    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SuccessKonfirmasi.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
        finish();
    }

    @OnClick(R.id.btnBackHome) void btnBackHome(){
        Intent intent = new Intent(SuccessKonfirmasi.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
        finish();
    }
}
