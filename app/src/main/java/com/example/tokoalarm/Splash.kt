package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val pref = PreferencesManager(applicationContext)
        val session = Session(pref)
        var intent : Intent
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            if (session.getPhone() == "" ) {
                intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            } else {
                intent = Intent(this, Main::class.java)
                startActivity(intent)
                finish()
            }
        },3000)
    }
}