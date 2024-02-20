package com.example.hw4.ui.theme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.hw4.MainActivity
import com.example.hw4.R

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        sendNotification(applicationContext)
        return Result.success()
    }

    private fun sendNotification(context: Context) {
        Log.d("Notifications", "Sending notification")
        val CHANNEL_ID = "channel_id"
        val NOTIFICATION_ID = 1
        val textTitle = "Alert!"
        val gx = inputData.getFloat("gx", 0f)
        val gy = inputData.getFloat("gy", 0f)
        val gz = inputData.getFloat("gz", 0f)
        val textContent = "The phone is spinning! Gyro data: gx=$gx, gy=$gy, gz=$gz"
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("destination", "messages")

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        builder.setContentIntent(pendingIntent)
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        )

        var notificationChannel = notificationManager?.getNotificationChannel(CHANNEL_ID)
        if (notificationChannel == null) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            notificationChannel = NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance)
            notificationChannel.enableVibration(true)
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            // notificationId is a unique int for each notification that you must define.
            notify(NOTIFICATION_ID, builder.build())
            Log.d("NotificationWorker", "Notification sent")
        }
    }
}