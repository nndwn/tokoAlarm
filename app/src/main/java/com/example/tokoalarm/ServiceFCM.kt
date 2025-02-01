package com.example.tokoalarm

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ServiceFCM : FirebaseMessagingService() {
    //todo :pada server fcm bersifat broadcast mengirim notifikasi ke seluruh penguna lalu app mengfilter pesan tersebut , metode tersebut tidak efektif jika memiliki banyak penguna akan menciptakan data antrian , FCM sendiri setiap client memiliki token masing-masing yang dapat di manfaatkan pengiriman di targetkan nantinya
    //todo : terdapat bug delay akibat melakukan broadcast solusi harus kirim nilai token ke server agar bisa langsung ke target

    private var countMsg : Int = 0

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            println(task.result)
        })
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notification = Notification(this)
        val data = message.notification
        notification.apply {
            if (data != null) {
                text = data.body.toString()
                title = data.title.toString()
                idMsg = countMsg++
            }
        }.create()
    }
}