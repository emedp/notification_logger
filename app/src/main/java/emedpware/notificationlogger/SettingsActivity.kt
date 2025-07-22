package emedpware.notificationlogger

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

private const val sharedPreferencesFileName = "SHARED_PREFERENCES"
private const val keyAlertDeletedNotifications = "ALERT_DELETED_NOTIFICATIONS"

class SettingsActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var switchDeletedNotification: SwitchMaterial

    // TODO: new setting to see a list of installed apps to select who listen or listen all by default
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = baseContext.getSharedPreferences(sharedPreferencesFileName, MODE_PRIVATE)
        val alertDeletedNotifications = preferences.getBoolean(keyAlertDeletedNotifications, false)
        Log.d("SWITCH_CHECKED", alertDeletedNotifications.toString())

        switchDeletedNotification = findViewById(R.id.switchDeletedNotification)
        switchDeletedNotification.isChecked = alertDeletedNotifications

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

    override fun onPause() {
        val editor = preferences.edit()
        editor.putBoolean(keyAlertDeletedNotifications, switchDeletedNotification.isChecked)
        editor.apply()
        Log.d(sharedPreferencesFileName,"")
        Log.d(keyAlertDeletedNotifications, switchDeletedNotification.isChecked.toString())
        super.onPause()
    }
}