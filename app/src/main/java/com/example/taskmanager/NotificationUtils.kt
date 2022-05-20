package com.example.taskmanager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class NotificationUtils {

    private val NOTIFICATION_CHANNEL_NAME ="Reminder Notifications"
    private val NOTIFICATION_CHANNEL_ID = "taskmanagerchannel"
    private val NOTIFICATION_CHANNEL_DESC = "Reminder of Tasks"

     @RequiresApi(Build.VERSION_CODES.O)
     fun createNotificationChannel(context: Context)
    {
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel : NotificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
        channel.description = NOTIFICATION_CHANNEL_DESC
        val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    @RequiresApi(Build.VERSION_CODES.M)
     fun scheduleNotification(task : Task, context: Context, time : Long)
    {
        val intent = Intent(context, NotificationBroadCast::class.java)
        val title = task.text
        val message = "Your task is still pending!"
        intent.putExtra("notificationTitle", task.text)
        intent.putExtra("notificationContent", message)
        val random_unique_id = (0..1000).random()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            random_unique_id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        Log.i("Notification Broadcast", "Pending intent id : " + random_unique_id)


        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

    }

}