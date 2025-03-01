package com.acm.newtokoalarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.launch

class A_ActivitySplash : AppCompatActivity() {
    private lateinit var alert : Z_DialogAlert
    private lateinit var utils: Z_Utils

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_splash)
        utils = Z_Utils(this)
        alert = Z_DialogAlert(this)

        lifecycleScope.launch {
            try{
                val response = RetrofitClient
                    .create(this@A_ActivitySplash)
                    .getDataApp()
                if (!response.isSuccessful) utils.error(Error.UNSUCCESS)
                val body = response.body()
                if (body?.code != 200) utils.error(Error.SERVERISSUE)
                val dataApp = body?.result
                val jwt = JWT(dataApp!!)

                val promoClaim = jwt.getClaim("promo").asList(Promo::class.java)
                println(promoClaim)

            } catch (e: Exception) {
                e.printStackTrace()
                alert.apply {
                    title = getString(R.string.error)
                    message = utils.messageError
                    animation = R.raw.error
                    btnText = getString(R.string.tutup)
                    show {
                        finish()
                    }
                }
            }
        }
    }
}