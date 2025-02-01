package com.example.tokoalarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

class Notification (private val context: Context){

    private var prefManager: PrefManager = PrefManager(context)
    private var nada: List<Tone> = DataManual().nada

    private var channelId :String = prefManager.getTone
    private var tone :Int = 0
    private var patternbVibrate = longArrayOf(0, 200, 200, 200, 500, 500, 500, 200, 200, 200, 1000)
    var title :String = ""
    var text :String = ""
    var idMsg : Int = 0

    fun create() {
        nada.forEach { value ->
            if ( value.name == channelId) tone = value.value
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val soundUri = Uri.parse("android.resource://" + context.packageName + "/" + tone)
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_channel_foreground)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

        val notificationBuilder =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_channel_foreground)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(text)
                .setVibrate(patternbVibrate)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)

        notificationManager.notify(idMsg, notificationBuilder.build())
    }

}