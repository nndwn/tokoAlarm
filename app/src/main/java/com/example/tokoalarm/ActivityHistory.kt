package com.example.tokoalarm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch

/**
 * Todo: Batasin fecth history
 *
 */


class ActivityHistory :AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener{


    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var historyView : RecyclerView
    private lateinit var session : PrefManager

    private var listHistory :List<ListTopUpData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        session = PrefManager(this)

        historyView = findViewById(R.id.adapterList)
        findViewById<TextView>(R.id.textMenu).text = getString(R.string.history)
        findViewById<View>(R.id.toolbar).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        swipeRefreshLayout = findViewById(R.id.containerSwipe)
        swipeRefreshLayout.setOnRefreshListener(this@ActivityHistory)
        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
            getData()
        }
    }

    private fun  troubleConnection() {
        val alert = DialogAlert(this)
        alert.apply {
            title = getString(R.string.info)
            message = getString(R.string.trouble_connection)
            animation = R.raw.crosserror
        }.show()
    }

    private fun getData() {
        lifecycleScope.launch {
            val response = RetrofitClient.apiService.getHistorySaldo(
                session.getIdUser!!
            )
            if (!response.isSuccessful) {
                troubleConnection()
                return@launch
            }
            val responseData = response.body()
            if (responseData?.status != true) {
                troubleConnection()
                return@launch
            }
            listHistory = responseData.data
            swipeRefreshLayout.isRefreshing = false
            historyView.layoutManager = LinearLayoutManager(this@ActivityHistory)
            historyView.adapter = AdapterListHistory(listHistory)

        }
    }

    override fun onRefresh() {
        getData()
    }
}