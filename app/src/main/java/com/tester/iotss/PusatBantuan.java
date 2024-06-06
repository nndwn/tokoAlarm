package com.tester.iotss;

import static com.tester.iotss.Configs.Config.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PusatBantuan extends AppCompatActivity {

    @BindView(R.id.tvFaq)
    TextView tvFaq;
    @BindView(R.id.tvNomorCs)
    TextView tvNomorCs;
    @BindView(R.id.tvNomorTeknisi)
    TextView tvNomorTeknisi;
    String nomor_cs="";
    String nomor_teknisi="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pusat_bantuan);
        ButterKnife.bind(this);

        getData();
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    private void getData(){
        AndroidNetworking.get(BASE_URL+"users/pusatbantuan")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        Log.d("PUSATBANTUANLOG",person.toString());
                        try {

                            boolean status = person.getBoolean("status");
                            if(status){
                                JSONObject data = person.getJSONObject("data");
                                tvFaq.setText(data.getString("faq"));
                                tvNomorCs.setText(data.getString("nomor_cs"));
                                tvNomorTeknisi.setText(data.getString("nomor_teknisi"));
                                nomor_cs = data.getString("nomor_cs");
                                nomor_teknisi = data.getString("nomor_teknisi");
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("PUSATBANTUANLOG",e.getMessage());
                            Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("PUSATBANTUANLOG",error.getMessage());
                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }


    @OnClick(R.id.lnChatCs) void lnChatCs(){
        String countryCode = "62";
        String formattedNumber = nomor_cs.replaceFirst("^0", countryCode);
        String url = "https://api.whatsapp.com/send?phone=" + formattedNumber;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}