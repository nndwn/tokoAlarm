package com.tester.svquickcount;

import static com.tester.svquickcount.Config.Config.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.tester.svquickcount.Dialog.AlertError;
import com.tester.svquickcount.Dialog.AlertSuccess;
import com.tester.svquickcount.Dialog.LoadingDialog;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class KonfirmasiPembayaran extends AppCompatActivity {


    String id_transaksi,grandtotal,invoice,total_bayar,total_item,potongan;
    @BindView(R.id.ivAttach)
    ImageView ivAttach;
    @BindView(R.id.ivBukti)
    ImageView ivBukti;

    SessionSetting sessionSetting;

    AlertError alertError;
    AlertSuccess alertSuccess;
    LoadingDialog loadingDialog;

    File fileGambar;

    @BindView(R.id.frameBukti)
    FrameLayout frameBukti;

    @BindView(R.id.btnKonfirmasi)
    LinearLayout btnKonfirmasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran);
        ButterKnife.bind(this);
        btnKonfirmasi.setEnabled(false);
        loadingDialog = new LoadingDialog(KonfirmasiPembayaran.this);
        alertError = new AlertError(KonfirmasiPembayaran.this);
        alertSuccess = new AlertSuccess(KonfirmasiPembayaran.this);
        sessionSetting = new SessionSetting();
        if(getIntent().hasExtra("id_transaksi")){
            id_transaksi = getIntent().getStringExtra("id_transaksi");
            grandtotal = getIntent().getStringExtra("grandtotal");
            invoice = getIntent().getStringExtra("invoice");
            total_bayar= getIntent().getStringExtra("total_bayar");
            total_item = getIntent().getStringExtra("total_item");
            potongan = getIntent().getStringExtra("potongan");
        }
    }


    @OnClick(R.id.ivAttach) void changePicture(){
        ImagePicker.Companion.with(this).cropSquare().galleryOnly().start();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK){
            final Uri selectedImage = imageReturnedIntent.getData();
            ivBukti.setImageURI(selectedImage);
            fileGambar = new File(selectedImage.getPath());
            ivAttach.setVisibility(View.GONE);
            frameBukti.setVisibility(View.VISIBLE);
            btnKonfirmasi.setEnabled(true);
            //prosesUpload();
        }
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @OnClick(R.id.btnDeleteGambar) void btnDeleteGambar(){
        fileGambar = null;
        ivAttach.setVisibility(View.VISIBLE);
        frameBukti.setVisibility(View.GONE);
        btnKonfirmasi.setEnabled(false);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);

    }


    @OnClick(R.id.btnKonfirmasi) void btnKonfirmasi(){
        loadingDialog.startLoadingDialog();
        AndroidNetworking.upload(BASE_URL+"webservice/transaksi/konfirmasipembayaran")
                .addMultipartParameter("id_transaksi",id_transaksi)
                .addMultipartParameter("grandtotal",grandtotal)
                .addMultipartFile("gambar",fileGambar)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject person) {
                        loadingDialog.dismissDialog();
                        try{
                            boolean status = person.getBoolean("status");
                            if(status){
                                Intent intent = new Intent(KonfirmasiPembayaran.this,SuccessKonfirmasi.class);
                                intent.putExtra("invoice",invoice);
                                intent.putExtra("total_bayar",total_bayar);
                                intent.putExtra("total_item",total_item);
                                intent.putExtra("potongan",potongan);
                                intent.putExtra("grandtotal",grandtotal);
                                intent.putExtra("message","Upload Bukti Berhasil");
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                            }else{
                                alertError.startDialog("Gagal",person.getString("message"));
                            }
                        }catch (JSONException e){
                            alertError.startDialog("Gagal",e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        loadingDialog.dismissDialog();
                        alertError.startDialog("Gagal",error.getMessage());
                    }
                });
    }
}
