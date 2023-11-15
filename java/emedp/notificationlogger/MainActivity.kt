package emedp.notificationlogger

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import emedp.notificationlogger.database.Database
import emedp.notificationlogger.database.NotificationAdapter
import emedp.notificationlogger.database.NotificationDAO

class MainActivity : AppCompatActivity() {
    private lateinit var dao: NotificationDAO
    private lateinit var listApps: List<String>
    private lateinit var listCategories: List<String>
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var tvTotal: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI
        tvTotal = findViewById(R.id.tv_total)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val fabRefresh: FloatingActionButton = findViewById(R.id.fab_refresh)
        val fabFilter: FloatingActionButton = findViewById(R.id.fab_filter)

        fabFilter.setOnClickListener { filterUI() }
        fabRefresh.setOnClickListener {
            refreshUI()
            Toast.makeText(this, getString(R.string.refreshed), Toast.LENGTH_SHORT).show()
        }

        // database
        val appDatabase = Database.getInstance(applicationContext)
        dao = appDatabase.notificationDAO()
        val listNotifications = dao.allNotifications

        // RecyclerView
        notificationAdapter = NotificationAdapter(listNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notificationAdapter

        refreshUI()
        requestAppPermissions()

        val firstTime = false
        // TODO: si es la primera vez que se abre la app: (Comprobar con SharedPreferences) enseñar instrucciones
        if (firstTime)
            openNotificationListenerSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_delete_all -> {
                alertDialogDelete()
                true
            }
            R.id.menu_open_setting -> {
                // TODO: abrir una nueva ventana que contenga todas las opciones necesarias para la escucha activa de notificaciones:
                /*
                TODO: ver como hace Mi Fit para ver lista de aplicaciones instaladas
                - abrir ajuste de notification listener
                - ahorro de energia
                
                 */
                openNotificationListenerSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun requestAppPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            ), 1)
        }
    }

    private fun openNotificationListenerSettings () {
        // TODO: cambiar para que no sea un AlertDialog si no que todo esto se vea desde una pantalla
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.setting_listener))
            .setMessage(getString(R.string.setting_listener_desc))
            .setNeutralButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.open_setting)) { _, _ -> startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")) }
            .show()
    }

    private fun filterUI () {
        val appsMap = ArrayMap<String, String>()
        val packageManager = applicationContext.packageManager

        try {
            val applications = packageManager.getInstalledApplications(0)
            Log.d("APPS",applications.toString())

            listApps.forEach {
                val appInfo = packageManager.getApplicationInfo(it, 0)
                val label = packageManager.getApplicationLabel(appInfo)
                Log.d("APP_PACKAGE", it)
                Log.d("LABEL", label.toString())

                appsMap[it] = label.toString()
            }
        } finally {
            Log.d("APPS_MAP", appsMap.toString())
        }

        val items = appsMap.values.toTypedArray()
        var itemChecked = 0

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.filter_app))
            .setSingleChoiceItems(items, itemChecked) { _, which -> itemChecked = which }
            .setNeutralButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .setNegativeButton(getString(R.string.clear)) { _, _ -> refreshUI() }
            .setPositiveButton(getString(R.string.apply)) { _, _ ->
                Log.d("ITEM_CHECKED", itemChecked.toString())
                if (notificationAdapter.itemCount != 0) {
                    val appPackage: String = appsMap.keys.toTypedArray()[appsMap.indexOfValue(items[itemChecked])]
                    notificationAdapter.refresh(dao.selectNotificationsWhereAppName(appPackage))
                    tvTotal.text = notificationAdapter.itemCount.toString()
                }
            }
            .show()
    }

    private fun refreshUI () {
        notificationAdapter.refresh(dao.allNotifications)
        tvTotal.text = notificationAdapter.itemCount.toString()

        listApps = dao.selectAppPackagesFromNotifications()
        listCategories = dao.selectCategoriesFromNotifications()
    }

    private fun clearUI () {
        dao.deleteNotificationsTable()
        refreshUI()
    }

    private fun alertDialogDelete () {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete_all))
            .setMessage(getString(R.string.delete_all_desc))
            .setNeutralButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.delete_all)) { _, _ -> clearUI() }
            .show()
    }
}