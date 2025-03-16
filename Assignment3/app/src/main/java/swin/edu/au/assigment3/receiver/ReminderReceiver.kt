package swin.edu.au.assigment3.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import swin.edu.au.assigment3.R

class ReminderReceiver : BroadcastReceiver()  {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "No title"
        val desc = intent.getStringExtra("desc") ?: ""

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "note_channel"

        // Tạo NotificationChannel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Note Reminder", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_time_24) // icon thông báo
            .setContentTitle(title)
            .setContentText(desc)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }


}