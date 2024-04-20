package emedp.notificationlogger

import android.icu.text.SimpleDateFormat
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import emedp.notificationlogger.database.Database
import emedp.notificationlogger.database.MyNotification
import java.util.Date
import java.util.Locale

class NotificationListener : NotificationListenerService() {

    private lateinit var appDatabase: Database

    /**
     * Implement this method to learn about when the listener is enabled and connected to
     * the notification manager.  You are safe to call [.getActiveNotifications]
     * at this time.
     */
    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NOTIFICATION_LISTENER","CONNECTED")
        val toastMessage = getString(R.string.service_name) + ": " + getString(R.string.connected)
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()

        appDatabase = Database.getInstance(baseContext)
    }

    /**
     * Implement this method to learn about when the listener is disconnected from the
     * notification manager.You will not receive any events after this call, and may only
     * call [.requestRebind] at this time.
     */
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d("NOTIFICATION_LISTENER","DISCONNECTED")
        val toastMessage = getString(R.string.service_name) + ": " + getString(R.string.disconnected)
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        Log.d("NOTIFICATION_LISTENER","DESTROY")
        super.onDestroy()
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
        Log.d("NOTIFICATION_LISTENER", "NOTIFICATION POSTED")
        if (sbn != null)
            recordStatusBarNotification(sbn)
    }

    /**
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
        Log.d("NOTIFICATION_LISTENER", "NOTIFICATION REMOVED")
    }

    private fun recordStatusBarNotification (sbn: StatusBarNotification) {
        Log.d("SBN", sbn.toString())
        // TODO: create a new notification of the notifications those are removed without user interaction
        val packageName = sbn.packageName
        val notification = sbn.notification

        val notificationWhen = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()).format(notification?.let { Date(it.`when`) })
        val notificationChannel = notification?.channelId.toString()
        val notificationCategory = notification?.category.toString()
        val extras = notification?.extras

        val title = extras?.getString("android.title").toString()
        val text = extras?.getString("android.text").toString()
        val subtext = extras?.getString("android.subText").toString()
        val textLines = extras?.getCharSequenceArrayList("android.textLines").toString()

        Log.d("PACKAGE_NAME", packageName)
        Log.d("NOTIFICATION", notification.toString())
        Log.d("NOTIFICATION_WHEN", notificationWhen)
        Log.d("NOTIFICATION_CHANNEL", notificationChannel)
        Log.d("NOTIFICATION_CATEGORY", notificationCategory)

        Log.d("EXTRAS",extras.toString())
        Log.d("EXTRAS_TITLE", title)
        Log.d("EXTRAS_TEXT", text)
        Log.d("EXTRAS_SUBTEXT", subtext)
        Log.d("EXTRAS_TEXT_LINES", textLines)

        val notificationText = if (sbn.isGroup) textLines else text

        // Insert into DB
        val notificationDB = MyNotification(
            packageName,
            notificationWhen,
            notificationChannel,
            notificationCategory,
            title,
            notificationText
        )
        appDatabase.notificationDAO().insertNotification(notificationDB)
    }
}