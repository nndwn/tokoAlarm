package com.example.tokoalarm

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

/**todo :pada server fcm bersifat broadcast mengirim notifikasi ke seluruh penguna lalu app
 *   mengfilter pesan tersebut , metode tersebut tidak efektif jika memiliki banyak penguna akan menciptakan data antrian , FCM sendiri setiap client memiliki token masing-masing yang dapat di
 *   manfaatkan pengiriman di targetkan nantinya
 *   terdapat bug delay akibat melakukan broadcast solusi harus kirim nilai token ke server agar bisa langsung ke target user
 */

/** pengaturian channel id tidak dapat di lakukan pada client harus FCM sendiri yang mengatur channel
 *  dapat di lakukan pada server untuk mengirim notifikasi channel di melalui FCM
 */

class ServiceFCM : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
        })
        DataManual().nada.forEach {
            Notification(this, it.name)
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println(token)
    }
}