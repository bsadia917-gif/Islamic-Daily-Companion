
package com.example.islamicdailycompanion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class PrayerNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val prayerName =
            intent.getStringExtra("prayer_name")
                ?: "Prayer"

        val channelId =
            "prayer_notifications"

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager


        // =====================================
        // CREATE NOTIFICATION CHANNEL
        // =====================================

        val channel =
            NotificationChannel(
                channelId,
                "Prayer Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {

                description =
                    "Notifications for daily prayer times"
            }

        notificationManager.createNotificationChannel(
            channel
        )


        // =====================================
        // NOTIFICATION
        // =====================================

        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(
                    android.R.drawable.ic_lock_idle_alarm
                )
                .setContentTitle(
                    "🕌 Prayer Time"
                )
                .setContentText(
                    "It's time for $prayerName prayer 🤲"
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(true)
                .build()


        // =====================================
        // SHOW NOTIFICATION
        // =====================================

        if (
            android.os.Build.VERSION.SDK_INT < 33 ||
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {

            NotificationManagerCompat
                .from(context)
                .notify(
                    prayerName.hashCode(),
                    notification
                )
        }
    }
}

