package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val pref = PrefSession(applicationContext)
        val session = Session(pref)
        var intent : Intent
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            if (session.getPhone() == "" || session.getPhone() == null) {
                intent = Intent(this, ActivityLogin::class.java)
                startActivity(intent)
                finish()
            } else {
                intent = Intent(this, ActivityMain::class.java)
                startActivity(intent)
                finish()
            }
        },3000)
    }
}