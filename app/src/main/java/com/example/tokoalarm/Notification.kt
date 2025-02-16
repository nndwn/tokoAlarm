package com.example.tokoalarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build

class Notification (context: Context, channelId: String){

    private var nada: List<Tone> = DataManual().nada

    private var tone :Int = 0
    private var patternbVibrate = longArrayOf(0, 200, 200, 200, 500, 500, 500, 200, 200, 200, 1000)

    init {
       tone = nada.find { it.name == channelId }!!.value
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val soundUri = Uri.parse("android.resource://" + context.packageName + "/" + tone)
        val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "notification chanel for sound $tone"
            enableLights(true)
            enableVibration(true)
            setSound(soundUri, AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build())
            setShowBadge(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setAllowBubbles(true)
            }
            setVibrationPattern(patternbVibrate)
        }
        notificationManager.createNotificationChannel(channel)
    }

}