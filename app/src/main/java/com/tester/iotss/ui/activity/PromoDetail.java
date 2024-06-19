package com.tester.iotss.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.iotss.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PromoDetail extends AppCompatActivity {

    @BindView(R.id.ivGambar)
    RoundedImageView ivGambar;
    @BindView(R.id.tvJudul)
    TextView tvJudul;
    @BindView(R.id.tvTanggalPublish)
    TextView tvTanggalPublish;
    @BindView(R.id.tvDeskripsi)
    TextView tvDeskripsi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_detail);
        ButterKnife.bind(this);
        if(getIntent().hasExtra("banner")){
            tvJudul.setText(getIntent().getStringExtra("title"));
            tvDeskripsi.setText(getIntent().getStringExtra("deskripsi"));
            tvTanggalPublish.setText(getIntent().getStringExtra("created_at"));
            Picasso.get().load(getIntent().getStringExtra("banner"))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.bannerplaceholder)
                    .into(ivGambar);
        }
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
    }
}