package com.example.tokoalarm

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ActivitySplash : AppCompatActivity() {

    private var listPromo = emptyList<ListPromo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            val pref = PrefManager(applicationContext)
            val session = Session(pref)
            delay(3000)
            if (session.getPhone().isNullOrEmpty()) {
                val intent = Intent(this@ActivitySplash, ActivityLogin::class.java)
                startActivity(intent)
                finish()
                return@launch
            }
            try {
                val response = RetrofitClient.apiService.getDataPelanggan(session.getIdUser()!!)
                if (!response.isSuccessful) throw Exception("Response not successful")
                val responseData = response.body()
                if (responseData?.status != true) throw Exception("Problem in status response")
                listPromo = responseData.data

                listPromo.forEach { url ->
                    val banner = Glide.with(this@ActivitySplash)
                        .asBitmap()
                        .load(url.banner)
                        .submit()
                    val filePath = withContext(Dispatchers.IO) {
                        saveImageToFile(banner.get(), "banner_${url.id}.png")
                    }
                    pref.setImagePaths(setOf(filePath))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent(this@ActivitySplash, ActivityMain::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveImageToFile(bitmap: Bitmap, filename: String): String {
        val file = File(filesDir, filename)
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }
}
