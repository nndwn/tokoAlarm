package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = PreferencesManager(applicationContext)
        val session = Session(pref)
        var intent : Intent
        setContentView(R.layout.splash)
        Handler(Looper.getMainLooper()).postDelayed({
            if (session.getPhone() == "" ) {
                intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            } else {
                intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }
        },3000)
    }
}