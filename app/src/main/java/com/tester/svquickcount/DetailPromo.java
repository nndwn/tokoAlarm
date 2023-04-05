package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class DetailPromo extends AppCompatActivity {


    @BindView(R.id.ivGambar)
    RoundedImageView ivGambar;
    @BindView(R.id.tvJudul)
    TextView tvJudul;
    @BindView(R.id.tvTanggalBerakhir)
    TextView tvTanggalBerakhir;
    @BindView(R.id.tvKodePromo)
    TextView tvKodePromo;
    @BindView(R.id.tvDeskripsi)
    TextView tvDeskripsi;

    String id_promo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promo);
        ButterKnife.bind(this);
        if(getIntent().hasExtra("gambar")){
            id_promo = getIntent().getStringExtra("id_promo");
            tvJudul.setText(getIntent().getStringExtra("judul"));
            tvKodePromo.setText(getIntent().getStringExtra("kodepromo"));
            tvDeskripsi.setText(getIntent().getStringExtra("deskripsi"));
            tvTanggalBerakhir.setText(getIntent().getStringExtra("tanggalberakhir"));
            Picasso.with(DetailPromo.this).load(getIntent().getStringExtra("gambar"))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.bannerplaceholder)
                    .into(ivGambar);
        }
    }


    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @OnClick(R.id.btnCopy) void btnCopy(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(tvKodePromo.getText().toString(), tvKodePromo.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(),"Kode berhasil dicopy!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
    }

}
