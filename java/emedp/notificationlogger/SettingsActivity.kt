package emedp.notificationlogger

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    // TODO: new setting to see a list of installed apps to select who listen or listen all by default
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bSettingsNotificationListener: (Button) = findViewById(R.id.b_settings_notification_listener)
        bSettingsNotificationListener.setOnClickListener { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }

        val bSettingsBatteryOptimization: (Button) = findViewById(R.id.b_settings_battery_optimization)
        bSettingsBatteryOptimization.setOnClickListener { startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)) }

        val bOpenSettings: (Button) = findViewById(R.id.b_open_settings)
        bOpenSettings.setOnClickListener { startActivity(Intent(Settings.ACTION_SETTINGS)) }

        val bSettingsAppDetails: (Button) = findViewById(R.id.b_settings_application_details)
        bSettingsAppDetails.setOnClickListener {
            startActivity(Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName")))
        }
    }
}