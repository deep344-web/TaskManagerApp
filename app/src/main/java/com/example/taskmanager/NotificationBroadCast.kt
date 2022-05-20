package com.example.taskmanager

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlin.concurrent.timerTask

class NotificationBroadCast() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val contentIntent : PendingIntent = PendingIntent.getActivity(context, 0,
            Intent(context, MainActivity::class.java), 0)

        val notification = NotificationCompat.Builder(context, "taskmanagerchannel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setContentTitle(intent.getStringExtra("notificationTitle"))
                .setContentText(intent.getStringExtra("notificationContent"))
                .build()

        val random_unique_id = (0..1000).random()

        val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(random_unique_id, notification)

        Log.i("Notification Broadcast", "notification id : " + random_unique_id)

    }
}


