package com.tester.svquickcount;

import static com.tester.svquickcount.Config.Config.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.Session.SessionLogin;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailTps extends AppCompatActivity {


    @BindView(R.id.tvKodeTps)
    TextView tvKodeTps;
    @BindView(R.id.tvNamaTps)
    TextView tvNamaTps;
    @BindView(R.id.tvProvinsi)
    TextView tvProvinsi;
    @BindView(R.id.tvKabupaten)
    TextView tvKabupaten;
    @BindView(R.id.tvKecamatan)
    TextView tvKecamatan;
    @BindView(R.id.tvKelurahan)
    TextView tvKelurahan;
    @BindView(R.id.tvAlamat)
    TextView tvAlamat;
    SessionLogin sessionLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tps);
        ButterKnife.bind(this);
        sessionLogin = new SessionLogin();
        try{
            JSONObject penugasan = new JSONObject(sessionLogin.getPenugasan(getApplicationContext()));
            boolean status = penugasan.getBoolean("status");
            if(status){
                JSONObject data = penugasan.getJSONObject("data");
                tvKodeTps.setText("#"+data.getString("id_tps"));
                tvNamaTps.setText(data.getString("nama_tps"));
                tvProvinsi.setText(data.getString("provinsi"));
                tvKabupaten.setText(data.getString("kabupaten"));
                tvKecamatan.setText(data.getString("kecamatan"));
                tvKelurahan.setText(data.getString("kelurahan"));
                tvAlamat.setText(data.getString("alamat"));
            }else{

            }
        }catch (Exception e){

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