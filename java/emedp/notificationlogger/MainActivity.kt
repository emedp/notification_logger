package emedp.notificationlogger

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import emedp.notificationlogger.database.Database
import emedp.notificationlogger.database.NotificationAdapter

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAppPermissions()

        val appDatabase = Database.getInstance(applicationContext)
        val dao = appDatabase.notificationDAO()

        // RecyclerView Adapter
        val notifications = dao.allNotifications
        val notificationAdapter = NotificationAdapter(notifications)

        // RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notificationAdapter

        val bOpenSettings: Button = findViewById(R.id.b_open_settings)
        bOpenSettings.setOnClickListener { openNotificationListenerSettings() }

        val bClearAll: Button = findViewById(R.id.b_clear)
        bClearAll.setOnClickListener {  } // TODO: put method to delete all
    }

    private fun requestAppPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            ), 1)
        }
    }

    private fun openNotificationListenerSettings () {
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }
}