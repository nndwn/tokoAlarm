package com.example.tokoalarm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch

class ActivityBeliPaket : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModel: SharedViewPilihPaket
    private lateinit var dialogAlert: DialogAlert
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_swipe_toolbar_fragment)

        dialogAlert = DialogAlert(this)

        swipeRefreshLayout = findViewById(R.id.containerSwipe)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
            getData()
        }

        viewModel = ViewModelProvider(this)[SharedViewPilihPaket::class.java]
        val saldoInt = intent.getStringExtra("saldo")?.let { parseSaldo(it) }
        viewModel.idAlat.value = intent.getStringExtra("idAlat")
        viewModel.saldo.value = saldoInt

        findViewById<TextView>(R.id.textMenu)
            .text = getString(R.string.buy_paket)
        findViewById<View>(R.id.toolbar)
            .setOnClickListener {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, FragmentPilihPaket())
                .commit()
        }
    }


    override fun onRefresh() {
        getData()
    }

    private fun errorConnection() {
        dialogAlert.apply {
            title = getString(R.string.info)
            message = getString(R.string.trouble_connection)
            animation = R.raw.crosserror
        }.show()
    }

    private fun getData() {
        lifecycleScope.launch {
            val response = RetrofitClient.apiService.getPaket()
            if (!response.isSuccessful) {
                errorConnection()
                return@launch
            }
            val data = response.body()
            if (data?.status != true) {
                errorConnection()
                return@launch
            }
            viewModel.paket.value = data.data
            swipeRefreshLayout.isRefreshing = false
        }
    }

}