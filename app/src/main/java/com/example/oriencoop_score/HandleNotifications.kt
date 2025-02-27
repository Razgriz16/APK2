package com.example.oriencoop_score

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.oriencoop_score.model.Notifications
import android.Manifest
import java.util.UUID

class HandleNotifications(private val context: Context) {
    private val channelId = "channel_id_01"
    private val notificationTag = "APP_NOTIFICATION" // For grouping

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = context.getString(R.string.notification_channel_name) // Localized
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                enableLights(true)
                lightColor = Color.RED
            }
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    fun showNotification(notification: Notifications) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            // Optionally trigger a permission request or log a message
            return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_alert_bellicon_top)
            .setContentTitle(notification.TITULO)
            .setContentText(notification.DESCRIPCION)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Affects pre-Oreo behavior
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            /*TODO BORRAR DE AQUI HASTA ABAJO ANTES DE SUBIR A PLAYSTORE*/
            .setVibrate(longArrayOf(0, 500)) // Explicitly trigger heads-up
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // Force sound
            .setFullScreenIntent(pendingIntent, true) // Use judiciously
            /*HASTA AQUI*/

        // Generate a unique ID (e.g., using UUID hashing)
        val notificationId = UUID.randomUUID().hashCode()
        context.getSystemService(NotificationManager::class.java)
            .notify(notificationTag, notificationId, builder.build())
    }
}