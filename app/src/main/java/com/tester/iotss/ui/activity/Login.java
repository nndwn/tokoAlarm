package com.tester.iotss.ui.activity;

import static com.tester.iotss.data.config.Config.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tester.iotss.R;
import com.tester.iotss.ui.dialog.AlertError;
import com.tester.iotss.ui.dialog.LoadingDialog;
import com.tester.iotss.utils.sessions.SessionLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Login extends AppCompatActivity {

    @BindView(R.id.edEmail)
    EditText edEmail;
    @BindView(R.id.edPassword)
    EditText edPassword;

    @BindView(R.id.checkSyaratKetentuan)
    CheckBox checkSyaratKetentuan;
    @BindView(R.id.tvSyaratKetentuan)
    TextView tvSyaratKetentuan;
    LoadingDialog loadingDialog;
    AlertError alertError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getData();
        loadingDialog = new LoadingDialog(Login.this);
        alertError = new AlertError(Login.this);
        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_WIFI_STATE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.READ_PHONE_STATE
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();

        checkSyaratKetentuan.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                tvSyaratKetentuan.setVisibility(View.VISIBLE);
            }else{
                tvSyaratKetentuan.setVisibility(View.GONE);
            }
        });
    }


    private Boolean validateEmail(){
        String val=edEmail.getText().toString();
        if(val.isEmpty()){
            edEmail.setError("Harap Isi Nohp Anda");
            return false;
        }else{
            edEmail.setError(null);
            return true;
        }
    }


    private Boolean validatepassword(){
        String val=edPassword.getText().toString();
        if(val.isEmpty()){
            edPassword.setError("Harap Isi Password");
            return false;
        }else{
            edPassword.setError(null);
            return true;
        }
    }

    @OnClick(R.id.btnDaftar) void btnDaftar(){
        Intent intent = new Intent(Login.this,Registrasi.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnLogin) void btnLogin() {
        if (!validatepassword() | !validateEmail()) {
            return;
        } else {
            if(checkSyaratKetentuan.isChecked()){
                loadingDialog.startLoadingDialog();
                AndroidNetworking.post(BASE_URL+"users/auth")
                        .addBodyParameter("nohp", edEmail.getText().toString())
                        .addBodyParameter("password", edPassword.getText().toString())
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject person) {
                                loadingDialog.dismissDialog();
                                Log.d("LOGINLOG",person.toString());
                                try {
                                    boolean isSuccess = person.getBoolean("status");
                                    if (isSuccess) {
                                        JSONObject data = person.getJSONObject("data");
                                        SessionLogin sessionLogin = new SessionLogin();
                                        sessionLogin.setId(data.getString("id"),getApplicationContext());
                                        sessionLogin.setNama(data.getString("nama"),getApplicationContext());
                                        sessionLogin.setNoHp(data.getString("nohp"),getApplicationContext());
                                        sessionLogin.setPassword(data.getString("password"),getApplicationContext());
                                        Intent intent = new Intent(Login.this,Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        alertError.startDialog("Login Gagal", person.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    alertError.startDialog("Login Gagal", e.getMessage());
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                loadingDialog.dismissDialog();
                                alertError.startDialog("Login Gagal", anError.getMessage());
                            }
                        });
            }else{
                alertError.startDialog("Info","Harap Ceklis Syarat & Ketentuan");
            }

        }
    }


    private void getData(){
        AndroidNetworking.get(BASE_URL+"users/syaratketentuan")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("LOGINLOG",person.toString());
                                try {

                                    boolean status = person.getBoolean("status");
                                    if(status){
                                        tvSyaratKetentuan.setText(person.getString("syarat_ketentuan"));
                                    }else{

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("LOGINLOG",e.getMessage());
                                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("LOGINLOG",error.getMessage());
                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}
