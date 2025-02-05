package com.example.tokoalarm

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.text.NumberFormat
import java.util.Locale


fun formatRupiah(number : Int) :String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(number)
}

/**
 *Todo: function ini dibuat karena api memberikan nilai
 * hasil yang sudah di konversi ke dalam Rupiah
 */
fun parseSaldo(saldo : String) :Int? {
    val cleanString = saldo.replace("[^0-9]".toRegex(), "")
    return cleanString.toIntOrNull()
}


fun convertDayName(numbStr: String): String {
    if (numbStr.isEmpty()) return ""
    val dayNames = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
    val numbArray = numbStr.split(",")
    return numbArray
        .mapNotNull { it.trim().toIntOrNull() }
        .filter { it in 1..7 }.joinToString(", ") { dayNames[it - 1] }
}

class Utils (private var context: Context){
    private var pref : PrefManager = PrefManager(context)

    /**
     * Todo: untuk nomor cs di buat secara hardcode api
     *  perlu diperbaikin kembali untuk menanganin configurasi app
     */
    fun whatsapp(message: String) {
        val packageManager = context.packageManager
        val isWhatsAppInstalled = try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }

        if (isWhatsAppInstalled) {
            val uri = Uri.parse("whatsapp://send?phone=6281394777794&text=${URLEncoder.encode(message, "UTF-8")}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }

    }

    suspend fun getBanner() {
        val response = RetrofitClient.apiService.getDataPelanggan(
            pref.getIdUser!!
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