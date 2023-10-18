package emedp.notificationlogger

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.Date

class NotificationListener : NotificationListenerService() {
    private lateinit var notificationManager: NotificationManager
    private val channelID = "emedpware.notificationlogger.notification_channel.removed_notifications"
    private var notificationID = 0

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        val notification = sbn!!.notification
        val notificationWhen = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date(notification.`when`))
        val notificationChannel = notification?.channelId.toString()
        val notificationCategory = notification?.category.toString()
        Log.d("NOTIFICATION", notification.toString())
        Log.d("NOTIFICATION_WHEN", notificationWhen)
        Log.d("NOTIFICATION_CHANNEL", notificationChannel)
        Log.d("NOTIFICATION_CATEGORY", notificationCategory)

        val extras = notification!!.extras
        val title = extras.getString("android.title").toString()
        val text = extras.getString("android.text").toString()
        val subtext = extras.getString("android.subText").toString()
        val textLines = extras.getString("android.textLines").toString()
        Log.d("EXTRAS",extras.toString())
        Log.d("EXTRAS_TITLE", title)
        Log.d("EXTRAS_TEXT", text)
        Log.d("EXTRAS_TEXT_LINES", textLines)
        Log.d("EXTRAS_SUBTEXT", subtext)

        if (notificationCategory == "msg") {
            createNotificationChannel() // TODO: mejorar esta estructura para que no se llame todo el tiempo
            when (textLines == "null") {
                true -> showNotification("[$notificationWhen] $title", text)
                false -> showNotification("[$notificationWhen] $title", "$text\n$textLines")
            }
        }
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.notification_channel_name)
        val desc = getString(R.string.notification_channel_desc)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        // Register the channel with the system
        notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification (title: String, message: String) {
        val notification = NotificationCompat.Builder(this, channelID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText(message)
            .build()
        notificationManager.notify(notificationID, notification)
        notificationID++
    }
}