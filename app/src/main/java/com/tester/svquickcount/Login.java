package com.tester.svquickcount;

import static com.tester.svquickcount.Config.Config.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tester.svquickcount.Dialog.AlertError;
import com.tester.svquickcount.Dialog.LoadingDialog;
import com.tester.svquickcount.Session.SessionLogin;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @BindView(R.id.edEmail)
    EditText edEmail;
    @BindView(R.id.edPassword)
    EditText edPassword;

    LoadingDialog loadingDialog;
    AlertError alertError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(Login.this);
        alertError = new AlertError(Login.this);
    }


    private Boolean validateEmail(){
        String val=edEmail.getText().toString();
        if(val.isEmpty()){
            edEmail.setError("Harap Isi Email Anda");
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

    @OnClick(R.id.btnLogin) void btnLogin() {
        if (!validatepassword() | !validateEmail()) {
            return;
        } else {
            loadingDialog.startLoadingDialog();
            SessionSetting sessionSetting = new SessionSetting();
            AndroidNetworking.post(BASE_URL+"webservice/auth")
                    .addBodyParameter("email", edEmail.getText().toString())
                    .addBodyParameter("password", edPassword.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject person) {
                            loadingDialog.dismissDialog();
                            try {
                                boolean isSuccess = person.getBoolean("status");
                                if (isSuccess) {
                                    JSONObject data = person.getJSONObject("data");
                                    SessionLogin sessionLogin = new SessionLogin();
                                    sessionLogin.setId_pelanggan(data.getString("id"),getApplicationContext());
                                    sessionLogin.setNama_pelanggan(data.getString("nama"),getApplicationContext());
                                    sessionLogin.setNohp_pelanggan(data.getString("nohp"),getApplicationContext());
                                    sessionLogin.setEmail_pelanggan(data.getString("email"),getApplicationContext());
                                    sessionLogin.setFoto_pelanggan(data.getString("foto"),getApplicationContext());
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
        }
    }
}
