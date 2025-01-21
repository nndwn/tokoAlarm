package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Home :AppCompatActivity() {
    private lateinit var logOutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        logOutBtn = findViewById(R.id.btnLogout)
        logOutBtn.setOnClickListener {
            val pref = PreferencesManager(applicationContext)
            val sessionLogin = Session(pref)
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            sessionLogin.logout()
            finish()
        }
    }
}