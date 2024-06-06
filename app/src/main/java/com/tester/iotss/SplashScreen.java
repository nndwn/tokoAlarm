package com.tester.iotss;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.tester.iotss.Session.SessionLogin;
import com.tester.iotss.Session.SessionSetting;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SessionLogin sessionLogin = new SessionLogin();

        SessionSetting sessionSetting = new SessionSetting();
        //sessionSetting.setEndpoint("http://192.168.10.122/tokobangunan/",getApplicationContext());
        new Handler().postDelayed(() -> {
            if(!sessionLogin.getId(getApplicationContext()).equals("")){
                Intent intent = new Intent(SplashScreen.this, Home.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
