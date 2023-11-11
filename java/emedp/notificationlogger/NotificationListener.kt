package emedp.notificationlogger

import android.icu.text.SimpleDateFormat
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import emedp.notificationlogger.database.Database
import emedp.notificationlogger.database.Notification
import java.util.Date
import java.util.Locale

class NotificationListener : NotificationListenerService() {

    private val appDatabase = Database.getInstance(baseContext)

    /**
     * Implement this method to learn about when the listener is enabled and connected to
     * the notification manager.  You are safe to call [.getActiveNotifications]
     * at this time.
     */
    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NOTIFICATION_LISTENER","connected")
        // TODO: string a xml
        Toast.makeText(this, "Notification Listener Service connected", Toast.LENGTH_SHORT).show()
        // TODO: cambiar por notificacion fijada
        // TODO: Pasar a foregound service (?)
        // TODO: copiar sistema de Mi Fitness para el reloj (inicio automático, etc)
        //https://developer.android.com/reference/android/app/Service#startForeground(int,%20android.app.Notification,%20int)
    }

    /**
     * Implement this method to learn about when the listener is disconnected from the
     * notification manager.You will not receive any events after this call, and may only
     * call [.requestRebind] at this time.
     */
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d("NOTIFICATION_LISTENER","disconnected")
        Toast.makeText(this, "Notification Listener Service disconnected", Toast.LENGTH_SHORT).show()
        // TODO: cambiar por notificacion fijada
        // TODO: string a xml
    }

    override fun getActiveNotifications(): Array<StatusBarNotification> {
        return super.getActiveNotifications()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("NOTIFICATION_LISTENER","DESTROY")
    }

    /**
     * Implement this method to learn about new notifications as they are posted by apps.
     *
     * @param sbn A data structure encapsulating the original [android.app.Notification]
     * object as well as its identifying information (tag and id) and source
     * (package name).
     */
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.d("NOTIFICATION_LISTENER","NOTIFICATION POSTED")

        val packageName = sbn?.packageName
        val notification = sbn?.notification
        val notificationWhen = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()).format(
            notification?.let { Date(it.`when`) })
        val notificationChannel = notification?.channelId.toString()
        val notificationCategory = notification?.category.toString()
        val extras = notification?.extras

        Log.d("PACKAGE_NAME", packageName.toString())
        Log.d("NOTIFICATION", notification.toString())
        Log.d("NOTIFICATION_WHEN", notificationWhen)
        Log.d("NOTIFICATION_CHANNEL", notificationChannel)
        Log.d("NOTIFICATION_CATEGORY", notificationCategory)

        val title = extras?.getString("android.title").toString()
        val text = extras?.getString("android.text").toString()
        val textLines = extras?.getCharSequence("android.textLines").toString()
        Log.d("EXTRAS",extras.toString())
        Log.d("EXTRAS_TITLE", title)
        Log.d("EXTRAS_TEXT", text)
        Log.d("EXTRAS_TEXT_LINES", textLines)

        val notificationText = when (textLines == "null") {
            true -> text
            false -> textLines
        }

        // Insert into DB
        val notificationDB = Notification(packageName, notificationWhen, notificationChannel, notificationCategory, title, notificationText)
        appDatabase.notificationDAO().insertNotification(notificationDB)
    }

    /**
     * Implement this method to learn when notifications are removed.
     *
     *
     * This might occur because the user has dismissed the notification using system UI (or another
     * notification listener) or because the app has withdrawn the notification.
     *
     *
     * NOTE: The [StatusBarNotification] object you receive will be "light"; that is, the
     * result from [StatusBarNotification.getNotification] may be missing some heavyweight
     * fields such as [android.app.Notification.contentView] and
     * [android.app.Notification.largeIcon]. However, all other fields on
     * [StatusBarNotification], sufficient to match this call with a prior call to
     * [.onNotificationPosted], will be intact.
     *
     * @param sbn A data structure encapsulating at least the original information (tag and id)
     * and source (package name) used to post the [android.app.Notification] that
     * was just removed.
     */
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("NOTIFICATION_LISTENER","NOTIFICATION REMOVED")
    }
}