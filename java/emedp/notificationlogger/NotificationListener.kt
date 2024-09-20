package emedp.notificationlogger

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import emedp.notificationlogger.database.Database
import emedp.notificationlogger.database.MyNotification
import java.util.Date
import java.util.Locale

class NotificationListener : NotificationListenerService() {

    private lateinit var appDatabase: Database
    private lateinit var notificationManager: NotificationManager
    private val notificationChannelID = "CHANNEL_ID_NOTILOG"
    private var notificationID = 0

    /**
     * Implement this method to learn about when the listener is enabled and connected to
     * the notification manager.  You are safe to call [.getActiveNotifications]
     * at this time.
     */
    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NOTIFICATION_LISTENER","CONNECTED")
        Toast.makeText(this, getString(R.string.connected), Toast.LENGTH_SHORT).show()

        appDatabase = Database.getInstance(baseContext)
        createNotificationChannel()
    }

    /**
     * Implement this method to learn about when the listener is disconnected from the
     * notification manager.You will not receive any events after this call, and may only
     * call [.requestRebind] at this time.
     */
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d("NOTIFICATION_LISTENER","DISCONNECTED")
        Toast.makeText(this, getString(R.string.disconnected), Toast.LENGTH_SHORT).show()
        // TODO requestRebind()
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
        Log.d("NOTIFICATION_POSTED", sbn.toString())
    }

    /**
     * Implement this method to learn when notifications are removed and why.
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
     * @param rankingMap The current ranking map that can be used to retrieve ranking information
     * for active notifications.
     * @param reason see [.REASON_LISTENER_CANCEL], etc.
     */
    override fun onNotificationRemoved(sbn: StatusBarNotification?, rankingMap: RankingMap?, reason: Int) {
        super.onNotificationRemoved(sbn, rankingMap, reason)
        val reasonText = reasonToString(reason)
        Log.d("NOTIFICATION_REMOVED_REASON", reasonText)

        if (sbn != null) {
            val notification = analyzeNotification(sbn)

            if (notification.appName != this.application.packageName) {
                // Insert into DB
                appDatabase.notificationDAO().insertNotification(notification)

                // ALERT USER WITH NOTIFICATION CODE BLOCK
                // category is MSG or null to be secure
                // reason of the notification removed should be app cancel or cancel all because try to manage remove notification of deleted message
                if (notification.category == null || notification.category == Notification.CATEGORY_MESSAGE) {
                    if (reason == REASON_APP_CANCEL || reason == REASON_APP_CANCEL_ALL) {
                        createNotification(
                            "${getString(R.string.alert_msg_deleted)} (${notification.title})",
                            "${notification.text}\nReason: $reasonText")
                    }
                }
            }
        }
    }

    private fun reasonToString (reason: Int): String {
        return when (reason) {
            REASON_APP_CANCEL -> "REASON_APP_CANCEL"
            REASON_APP_CANCEL_ALL -> "REASON_APP_CANCEL_ALL"
            REASON_ASSISTANT_CANCEL -> "REASON_ASSISTANT_CANCEL"
            REASON_CANCEL -> "REASON_CANCEL"
            REASON_CANCEL_ALL -> "REASON_CANCEL_ALL"
            REASON_CLICK -> "REASON_CLICK"
            REASON_CHANNEL_BANNED -> "REASON_CHANNEL_BANNED"
            REASON_CHANNEL_REMOVED -> "REASON_CHANNEL_REMOVED"
            REASON_ERROR -> "REASON_ERROR"
            REASON_GROUP_OPTIMIZATION -> "REASON_GROUP_OPTIMIZATION"
            REASON_GROUP_SUMMARY_CANCELED -> "REASON_GROUP_SUMMARY_CANCELED"
            REASON_LISTENER_CANCEL -> "REASON_LISTENER_CANCEL"
            REASON_LISTENER_CANCEL_ALL -> "REASON_LISTENER_CANCEL_ALL"
            REASON_LOCKDOWN -> "REASON_LOCKDOWN"
            REASON_PACKAGE_BANNED -> "REASON_PACKAGE_BANNED"
            REASON_PACKAGE_CHANGED -> "REASON_PACKAGE_CHANGED"
            REASON_PACKAGE_SUSPENDED -> "REASON_PACKAGE_SUSPENDED"
            REASON_PROFILE_TURNED_OFF -> "REASON_PROFILE_TURNED_OFF"
            REASON_SNOOZED -> "REASON_SNOOZED"
            REASON_TIMEOUT -> "REASON_TIMEOUT"
            REASON_UNAUTOBUNDLED -> "REASON_UNAUTOBUNDLED"
            REASON_USER_STOPPED -> "REASON_USER_STOPPED"
            else -> "REASON CODE: $reason"
        }
    }

    private fun analyzeNotification(sbn: StatusBarNotification): MyNotification {
        Log.d("SBN", sbn.toString())

        val packageName = sbn.packageName
        val notification = sbn.notification
        // notification variables
        val formattedWhen = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()).format(Date(notification.`when`))
        val notificationWhen = formattedWhen
        val notificationChannel = notification?.channelId.toString()
        val notificationCategory = notification?.category.toString()
        val extras = notification.extras
        // extras variables
        val title = extras.get("android.title").toString()
        val text = extras.get("android.text").toString()
        val subText = extras.get("android.subText").toString()
        val textLines = extras.get("android.textLines").toString()
        var notificationText = ""

        if (text != "null")
            notificationText += text + "\n"
        if (subText != "null")
            notificationText += subText + "\n"
        if (textLines != "null")
            notificationText += textLines

        Log.d("NOTIFICATION", notification.toString())
        Log.d("PACKAGE_NAME", packageName)
        Log.d("NOTIFICATION_WHEN", notificationWhen)
        Log.d("NOTIFICATION_CHANNEL", notificationChannel)
        Log.d("NOTIFICATION_CATEGORY", notificationCategory)
        Log.d("EXTRAS", extras.toString())
        Log.d("EXTRAS_TITLE", title)
        Log.d("EXTRAS_TEXT", text)
        Log.d("EXTRAS_SUBTEXT", subText)
        Log.d("EXTRAS_TEXT_LINES", textLines)

        return MyNotification(
            packageName,
            notificationWhen,
            notificationChannel,
            notificationCategory,
            title,
            notificationText
        )
    }

    private fun createNotificationChannel() {
        val notificationChannelName = "CHANNEL_NAME_NOTILOG"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(notificationChannelID, notificationChannelName, importance)
        // Register the channel with the system
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun createNotification (title: String, text: String) {
        val notification = NotificationCompat.Builder(this, notificationChannelID)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setShowWhen(true)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(notificationID, notification)
        notificationID++
    }
}