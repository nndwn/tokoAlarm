package com.example.tokoalarm

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch

class ActivityHistory :AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener{

    private lateinit var iconMenuToolbar : ImageView
    private lateinit var texMenuToolbar : TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var session : Session
    private lateinit var historyView : RecyclerView


    private var listHistory :List<ListTopUpData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        session = Session(PrefManager(this))

        historyView = findViewById(R.id.fragment_container_history)
        iconMenuToolbar = findViewById(R.id.iconMenu)
        texMenuToolbar = findViewById(R.id.textMenu)
        texMenuToolbar.text = getString(R.string.history)
        iconMenuToolbar.setOnClickListener {
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
        alert.show(
            getString(R.string.info),
            getString(R.string.trouble_connection),
            R.raw.crosserror
        )
    }

    private fun getData() {
        lifecycleScope.launch {
            val response = RetrofitClient.apiService.getHistorySaldo(
                session.getIdUser()!!
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