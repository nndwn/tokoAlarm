package com.acm.newtokoalarm

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.auth0.android.jwt.JWT
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList

/**
 * - start flash ui
 * - chekc connection if error give alert and out
 * - get all data app and save reference dataStore
 * - take from data promo from data app
 * - download image banner promo and save in persitance location path
 * - check have authenfication in reference dataStore
 * - if dont have go login activity else to main activity
 * - in login sent intent data url from data app
 * - dont go back
 */

class AActivitySplash : AppCompatActivity() {
    private lateinit var dialog: GDialog
    private lateinit var utils: GUtils
    private lateinit var pref: GDataStore
    private lateinit var jwt: JWT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_splash)

        utils = GUtils(this)
        dialog = GDialog(this)
        pref = GDataStore(this)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient
                    .create(this@AActivitySplash)
                    .getDataApp()
                if (!response.isSuccessful) utils.error(Error.UNSUCCESS)
                val body = response.body()
                if (body?.code != 200) utils.error(Error.SERVERISSUE)
                val dataApp = body?.result!!

                pref.setAppData(dataApp)
                jwt = JWT(dataApp)

                bannerPromo()
                //todo: check authenfication

                val intent = Intent(this@AActivitySplash, BActivityLogin::class.java)
                val dataUrl = jwt.getClaim("appUrl").asList(AppUrl::class.java)
                intent.putParcelableArrayListExtra("dataUrl", ArrayList(dataUrl))
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                val alert = DialogData(
                    title = getString(R.string.error),
                    message = utils.messageError,
                    animation = R.raw.error,
                    btnText = getString(R.string.tutup)
                )
                dialog.alert(alert) {
                    finish()
                }
            }
        }
    }

    private suspend fun bannerPromo() {
        val promoClaim = jwt.getClaim("promo").asList(Promo::class.java)
        promoClaim.forEach {
            val banner = Glide.with(this)
                .asBitmap()
                .load( "${this.getString(R.string.static_url)}${it.image}")
                .submit()
            val content = this.filesDir
            withContext(Dispatchers.IO){
                val file = File(content, it.image)
                FileOutputStream(file).use { out ->
                    banner.get().compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                }
                pref.setPromoBanner(setOf(file.absolutePath))
            }

        }
    }
}
