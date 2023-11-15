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
        if (firstTime) {
            // TODO: si es la primera vez que se abre la app: (Comprobar con SharedPreferences) enseñar instrucciones
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete_all -> {
                alertDialogDelete()
                true
            }
            R.id.menu_open_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
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
                    val itemSelected: String = items[itemChecked]
                    val appPackage: String = appsMap.keys.toTypedArray()[itemChecked]
                    Log.d("SELECTED_APP_NAME", itemSelected)
                    Log.d("SELECTED_APP_PACKAGE", appPackage)
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