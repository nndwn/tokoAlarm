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

public class UbahPassword extends AppCompatActivity {

    @BindView(R.id.edPasswordLama)
    EditText edPasswordLama;
    @BindView(R.id.edPasswordBaru)
    EditText edPasswordBaru;
    LoadingDialog loadingDialog;
    AlertError alertError;
    AlertSuccess alertSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);
        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(UbahPassword.this);
        alertError = new AlertError(UbahPassword.this);
        alertSuccess = new AlertSuccess(UbahPassword.this);
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
    }

    private Boolean validatepasswordlama(){
        String val=edPasswordLama.getText().toString();
        if(val.isEmpty()){
            edPasswordLama.setError("Harap Isi Password Lama Anda");
            return false;
        }else{
            edPasswordLama.setError(null);
            return true;
        }
    }

    private Boolean validatepasswordbaru(){
        String val=edPasswordBaru.getText().toString();
        if(val.isEmpty()){
            edPasswordBaru.setError("Harap Isi Password Baru Anda");
            return false;
        }else{
            edPasswordBaru.setError(null);
            return true;
        }
    }


    @OnClick(R.id.btnUbahPassword) void btnUbahPassword(){
        if(!validatepasswordlama() | !validatepasswordbaru()){
            return;
        }else {
            loadingDialog.startLoadingDialog();
            SessionSetting sessionSetting = new SessionSetting();
            SessionLogin sessionLogin = new SessionLogin();
            AndroidNetworking.post(BASE_URL+"webservice/auth/ubahpassword")
                    .addBodyParameter("id",sessionLogin.getId(getApplicationContext()))
                    .addBodyParameter("password_lama", edPasswordLama.getText().toString())
                    .addBodyParameter("password_baru", edPasswordBaru.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject person) {
                            loadingDialog.dismissDialog();
                            try {
                                boolean isSuccess = person.getBoolean("status");
                                if (isSuccess) {
                                    alertSuccess.startDialog("Berhasil","Ubah Password Berhasil");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            onBackPressed();
                                        }
                                    },1500);
                                } else {
                                    alertError.startDialog("Ubah Password Gagal", person.getString("message"));
                                }
                            } catch (JSONException e) {
                                alertError.startDialog("Ubah Password Gagal", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            loadingDialog.dismissDialog();
                            alertError.startDialog("Ubah Password Gagal", anError.getMessage());
                        }
                    });
        }
    }
}
