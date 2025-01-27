package com.example.tokoalarm

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale


class Utils (private var context: Context){
    private var pref : PrefManager = PrefManager(context)
    private var session: Session = Session(pref)


    suspend fun getBanner() {


        val response = RetrofitClient.apiService.getDataPelanggan(
            session.getIdUser()!!
        )
        if (!response.isSuccessful) throw Exception("Response not successful")
        val responseData = response.body()
        if (responseData?.status != true) throw Exception("Problem in status response")
        val list = responseData.data

        list.forEach { url ->
            val banner = Glide.with(context)
                .asBitmap()
                .load(url.banner)
                .submit()
            val file = withContext(Dispatchers.IO) {
                saveImageToFile(banner.get(), "banner_${url.id}.png")
            }
            pref.setImagePaths(setOf(file))
        }
    }
    fun formatRupiah(number : Int) :String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(number)
    }
    private suspend fun saveImageToFile(bitmap: Bitmap, filename: String) : String {
        val file = File( context.filesDir, filename)
        withContext(Dispatchers.IO) {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
            }
        }
        return file.absolutePath
    }

}