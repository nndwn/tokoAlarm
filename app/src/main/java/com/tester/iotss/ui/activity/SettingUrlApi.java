package com.tester.iotss.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.widget.EditText;

import com.tester.iotss.R;
import com.tester.iotss.utils.sessions.SessionSetting;

public class SettingUrlApi extends AppCompatActivity {

    @BindView(R.id.edEndpoint)
    EditText edEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_url_api);
        ButterKnife.bind(this);
        SessionSetting sessionSetting = new SessionSetting();
        if(!sessionSetting.getEndpoint(getApplicationContext()).equals("")){
            edEndpoint.setText(sessionSetting.getEndpoint(getApplicationContext()));
        }
    }


    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    private Boolean validateUri(){
        String val=edEndpoint.getText().toString();
        if(val.isEmpty()){
            edEndpoint.setError("Tidak boleh kosong");
            return false;
        }else{
            edEndpoint.setError(null);
            return true;
        }
    }

    @OnClick(R.id.btnSimpan) void btnSimpan() {
        if (!validateUri()) {
            return;
        } else {
            SessionSetting sessionSetting = new SessionSetting();
            sessionSetting.setEndpoint(edEndpoint.getText().toString(),getApplicationContext());
            onBackPressed();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
    }
}
