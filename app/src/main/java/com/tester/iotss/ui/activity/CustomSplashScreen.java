package com.tester.iotss.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.tester.iotss.R;
import com.tester.iotss.utils.sessions.SessionLogin;
import com.tester.iotss.utils.sessions.SessionSetting;

public class CustomSplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        SessionLogin sessionLogin = new SessionLogin();
        SessionSetting sessionSetting = new SessionSetting();
        new Handler().postDelayed(() -> {
            if(!sessionLogin.getId(getApplicationContext()).isEmpty()){
                Intent intent = new Intent(CustomSplashScreen.this, Home.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(CustomSplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
