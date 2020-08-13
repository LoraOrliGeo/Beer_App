package com.example.beerapp.presenters.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.beerapp.R
import com.example.beerapp.ui.SplashScreenActivity
import java.text.SimpleDateFormat
import java.util.*

const val WORK_TAG = "NotificationWorker"

class NotificationsWorker(appContext: Context, params: WorkerParameters) :
    Worker(appContext, params) {
    override fun doWork(): Result {
        triggerNotification()
        return Result.SUCCESS
    }

    private fun triggerNotification() {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelId = applicationContext.getString(R.string.notification_channel_id)

        val title = applicationContext.getString(R.string.notification_title)
        val body = applicationContext.getString(R.string.notification_body)

        val intent = Intent(applicationContext, SplashScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.beer_bottle)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setChannelId(channelId)
            .build()

        with(NotificationManagerCompat.from(applicationContext)) {
            if (getNotificationChannel(channelId) == null) {
                // create a channel
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val name = applicationContext.getString(R.string.notification_channel_name)
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel = NotificationChannel(channelId, name, importance)
                    notificationManager.createNotificationChannel(channel)
                }
            }

            notificationManager.notify(createUniqueId(), notification)
        }
    }

    private fun createUniqueId(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.getDefault()).format(now).toInt()
    }
}