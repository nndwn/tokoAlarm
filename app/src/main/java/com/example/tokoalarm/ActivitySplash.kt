package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivitySplash : AppCompatActivity() {

    private lateinit var alert: DialogAlert
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_splash)
        val utils = Utils(this)
        alert = DialogAlert(this)
        lifecycleScope.launch {
            val pref = PrefManager(applicationContext)
            delay(3000)
            if (pref.getPhone.isNullOrEmpty()) {
                val intent = Intent(this@ActivitySplash, ActivityLogin::class.java)
                startActivity(intent)
                finish()
                return@launch
            }
            try {
                utils.getBanner()
            } catch (e: Exception) {
                e.printStackTrace()
                alert.apply {
                    title = getString(R.string.perhatian)
                    message = getString(R.string.trouble_connection)
                    animation = R.raw.crosserror

                }.show {
                    finish()
                }
                return@launch
            }

            val intent = Intent(this@ActivitySplash, ActivityMain::class.java)
            startActivity(intent)
            finish()
        }
    }
}
