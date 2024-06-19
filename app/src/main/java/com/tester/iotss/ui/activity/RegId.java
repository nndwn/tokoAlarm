package com.tester.iotss.ui.activity;

import static com.tester.iotss.data.config.Config.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tester.iotss.R;
import com.tester.iotss.ui.dialog.AlertError;
import com.tester.iotss.ui.dialog.AlertSuccess;
import com.tester.iotss.ui.dialog.LoadingDialog;
import com.tester.iotss.utils.sessions.SessionLogin;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegId extends AppCompatActivity {

    @BindView(R.id.edIdAlat)
    EditText edIdAlat;
    LoadingDialog loadingDialog;
    AlertError alertError;
    AlertSuccess alertSuccess;

    SessionLogin sessionLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_id);
        ButterKnife.bind(this);
        sessionLogin = new SessionLogin();
        loadingDialog = new LoadingDialog(RegId.this);
        alertError = new AlertError(RegId.this);
        alertSuccess = new AlertSuccess(RegId.this);
        edIdAlat.setText(sessionLogin.getId_alat(getApplicationContext()));
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
    }

    private Boolean validateIdAlat(){
        String val=edIdAlat.getText().toString();
        if(val.isEmpty()){
            edIdAlat.setError("Harap Isi ID Alat Anda");
            return false;
        }else{
            edIdAlat.setError(null);
            return true;
        }
    }


    @OnClick(R.id.btnSimpan) void btnSimpan(){
        if(!validateIdAlat()){
            return;
        }else {
            loadingDialog.startLoadingDialog();
            AndroidNetworking.post(BASE_URL+"users/book")
                    .addBodyParameter("nohp",sessionLogin.getNohp(getApplicationContext()))
                    .addBodyParameter("id_alat", edIdAlat.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject person) {
                            loadingDialog.dismissDialog();
                            try {
                                boolean isSuccess = person.getBoolean("status");
                                if (isSuccess) {
                                    sessionLogin.setId_alat(edIdAlat.getText().toString(),getApplicationContext());
                                    alertSuccess.startDialog("Berhasil","ID Alat Berhasil Dihubungkan");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            onBackPressed();
                                        }
                                    },1500);
                                } else {
                                    alertError.startDialog("Gagal Menghubungkan Id Alat", person.getString("message"));
                                }
                            } catch (JSONException e) {
                                alertError.startDialog("Gagal Menghubungkan Id Alat", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            loadingDialog.dismissDialog();
                            alertError.startDialog("Gagal Menghubungkan Id Alat", anError.getMessage());
                        }
                    });
        }
    }
}