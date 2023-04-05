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
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONException;
import org.json.JSONObject;

public class Registrasi extends AppCompatActivity {


    @BindView(R.id.edNama)
    EditText edNama;
    @BindView(R.id.edEmail)
    EditText edEmail;
    @BindView(R.id.edHp)
    EditText edHp;
    @BindView(R.id.edPassword)
    EditText edPassword;


    LoadingDialog loadingDialog;
    AlertError alertError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        ButterKnife.bind(this);

        loadingDialog = new LoadingDialog(Registrasi.this);
        alertError = new AlertError(Registrasi.this);
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
    }


    private Boolean validateNama(){
        String val=edNama.getText().toString();
        if(val.isEmpty()){
            edNama.setError("Harap Isi Nama Anda");
            return false;
        }else{
            edNama.setError(null);
            return true;
        }
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

    private Boolean validateHp(){
        String val=edHp.getText().toString();
        if(val.isEmpty()){
            edHp.setError("Harap Isi Email Anda");
            return false;
        }else{
            edHp.setError(null);
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
        if(!validateNama() | !validatepassword() | !validateEmail() | !validateHp()){
            return;
        }else {
            loadingDialog.startLoadingDialog();
            SessionSetting sessionSetting = new SessionSetting();
            AndroidNetworking.post(BASE_URL+"webservice/auth/regis")
                    .addBodyParameter("nama",edNama.getText().toString())
                    .addBodyParameter("email", edEmail.getText().toString())
                    .addBodyParameter("hp",edHp.getText().toString())
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
                                    Intent intent = new Intent(Registrasi.this,SuccessRegistrasi.class);
                                    intent.putExtra("nama",edNama.getText().toString());
                                    intent.putExtra("email",edEmail.getText().toString());
                                    intent.putExtra("hp",edHp.getText().toString());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
                                    finish();
                                } else {
                                    alertError.startDialog("Registrasi Gagal", person.getString("message"));
                                }
                            } catch (JSONException e) {
                                alertError.startDialog("Registrasi Gagal", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            loadingDialog.dismissDialog();
                            alertError.startDialog("Registrasi Gagal", anError.getMessage());
                        }
                    });
        }
    }
}
