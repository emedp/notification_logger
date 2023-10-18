package emedp.notificationlogger

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bOpenSettings: Button = findViewById(R.id.b_open_settings)
        bOpenSettings.setOnClickListener { openNotificationListenerSettings() }

        requestAppPermissions()
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