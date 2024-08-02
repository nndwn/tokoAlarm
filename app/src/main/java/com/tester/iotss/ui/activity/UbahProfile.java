package com.tester.iotss.ui.activity;

import static com.tester.iotss.data.config.Config.BASE_URL;

import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tester.iotss.R;
import com.tester.iotss.ui.dialog.AlertError;
import com.tester.iotss.ui.dialog.AlertSuccess;
import com.tester.iotss.ui.dialog.LoadingDialog;
import com.tester.iotss.utils.sessions.SessionLogin;
import com.tester.iotss.utils.sessions.SessionSetting;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UbahProfile extends AppCompatActivity {


    @BindView(R.id.edNama)
    EditText edNama;
    @BindView(R.id.edEmail)
    EditText edEmail;
    @BindView(R.id.edHp)
    EditText edHp;

    SessionLogin sessionLogin;
    LoadingDialog loadingDialog;
    AlertError alertError;
    AlertSuccess alertSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profile);
        ButterKnife.bind(this);
        sessionLogin = new SessionLogin();
        loadingDialog = new LoadingDialog(UbahProfile.this);
        alertError = new AlertError(UbahProfile.this);
        alertSuccess=new AlertSuccess(UbahProfile.this);

        edNama.setText(sessionLogin.getNama(getApplicationContext()));
        edEmail.setText(sessionLogin.getNohp(getApplicationContext()));
        edHp.setText(sessionLogin.getNohp(getApplicationContext()));

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


    @OnClick(R.id.btnSimpan) void btnSimpan() {
        if (!validateNama() | !validateEmail() | !validateHp()) {
            return;
        } else {
            loadingDialog.startLoadingDialog();
            SessionSetting sessionSetting = new SessionSetting();
            SessionLogin sessionLogin = new SessionLogin();
            AndroidNetworking.post(BASE_URL+"webservice/auth/ubahprofile")
                    .addBodyParameter("id",sessionLogin.getId(getApplicationContext()))
                    .addBodyParameter("nama", edNama.getText().toString())
                    .addBodyParameter("nohp", edHp.getText().toString())
                    .addBodyParameter("email", edEmail.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject person) {
                            loadingDialog.dismissDialog();
                            try {
                                boolean isSuccess = person.getBoolean("status");
                                if (isSuccess) {

                                    sessionLogin.setNoHp(edHp.getText().toString(),getApplicationContext());
                                    sessionLogin.setNama(edEmail.getText().toString(),getApplicationContext());
                                    sessionLogin.setNama(edNama.getText().toString(),getApplicationContext());
                                    alertSuccess.startDialog("Berhasil","Ubah Data Berhasil");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            onBackPressed();
                                        }
                                    },1500);
                                } else {
                                    alertError.startDialog("Ubah Data Gagal", person.getString("message"));
                                }
                            } catch (JSONException e) {
                                alertError.startDialog("Ubah Data Gagal", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            loadingDialog.dismissDialog();
                            alertError.startDialog("Ubah Data Gagal", anError.getMessage());
                        }
                    });
        }
    }
}
