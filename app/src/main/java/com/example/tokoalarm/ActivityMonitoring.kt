package com.example.tokoalarm

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch


class ActivityMonitoring : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var listAlat: ListAlat
    private var listJadwal = ArrayList<ListJadwal>()
    private lateinit var viewMonitoring: SharedViewMonitoring
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_swipe_toolbar_fragment)
        viewMonitoring = ViewModelProvider(this)[SharedViewMonitoring::class.java]
        prefManager = PrefManager(this)

        swipeRefreshLayout = findViewById(R.id.containerSwipe)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
            getData()
        }
        findViewById<TextView>(R.id.textMenu).text = getString(R.string.monitoring)
        findViewById<View>(R.id.toolbar).setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else onBackPressedDispatcher.onBackPressed()
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

    private fun getData() {
        lifecycleScope.launch {
            val response = RetrofitClient.apiService.getDetailSettingAlat(
                prefManager.getPhone!!
                , listAlat.idAlat
                , listAlat.nomorPaket
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    viewMonitoring.mode.value = it.lastAlat.data.mode
                }
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }


    override fun onRefresh() {
        getData()
    }
}