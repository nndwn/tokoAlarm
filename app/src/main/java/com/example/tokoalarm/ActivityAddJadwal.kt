package com.example.tokoalarm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ActivityAddJadwal : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener{

    private lateinit var sharedViewAddJadwal: SharedViewAddJadwal

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_swipe_toolbar_fragment)
        findViewById<TextView>(R.id.textMenu).text = getString(R.string.tambah_jadwal)
        findViewById<View>(R.id.toolbar).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.containerSwipe)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
        }

        if (savedInstanceState == null) {
            val fragment = FragmentListAddJadwal()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit()
        }
    }
    override fun onRefresh() {

    }
}