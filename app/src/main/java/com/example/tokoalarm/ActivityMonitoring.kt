package com.example.tokoalarm

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class ActivityMonitoring : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener {
    private lateinit var listAlat: ListAlat
    private var listJadwal = ArrayList<ListJadwal>()
    private val viewMonitoring: ViewModelMonitor by viewModels()
    private lateinit var prefManager: PrefManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_swipe_toolbar_fragment)

        prefManager = PrefManager(this)

        findViewById<TextView>(R.id.textMenu).text = getString(R.string.monitoring)
        findViewById<View>(R.id.toolbar).setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else onBackPressedDispatcher.onBackPressed()
       }

        swipeRefreshLayout = findViewById(R.id.containerSwipe)
        swipeRefreshLayout.setOnRefreshListener(this@ActivityMonitoring)
        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
            viewMonitoring.connectMqtt()
            viewMonitoring.getData(
                prefManager.getPhone!!
                , listAlat.idAlat
                , listAlat.nomorPaket
            )
        }

        listAlat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           intent.getParcelableExtra("MonitorData", ListAlat::class.java)!!
       } else {
           @Suppress("DEPRECATION")
            intent.getParcelableExtra("MonitorData")!!
        }

        viewMonitoring.alat.value = listAlat

        listJadwal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("JadwalData", ListJadwal::class.java)!!
        } else {
           @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("JadwalData")!!
       }

        listJadwal.filter {
            it.idAlat == listAlat.idAlat
        }.forEach {
            viewMonitoring.jadwal.value = it
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, FragmentMonitoring())
                .commit()
        }
    }


    override fun onResume() {
        super.onResume()
        viewMonitoring.refresh.observe(this) {
            swipeRefreshLayout.isRefreshing = it

        }
    }

    override fun onRefresh() {
        viewMonitoring.getData(
            prefManager.getPhone!!
            , listAlat.idAlat
            , listAlat.nomorPaket
        )
        viewMonitoring.connectMqtt()
    }

}