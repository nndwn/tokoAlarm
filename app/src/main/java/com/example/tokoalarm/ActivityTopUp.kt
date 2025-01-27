package com.example.tokoalarm

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class ActivityTopUp :AppCompatActivity(){
    private lateinit var viewModel: SharedViewTopUp
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        viewModel = ViewModelProvider(this)[SharedViewTopUp::class.java]

        val iconMenuToolbar = findViewById<ImageView>(R.id.iconMenu)
        val titleToolbar = findViewById<TextView>(R.id.textMenu)
        titleToolbar.text = getString(R.string.isi_paket)
        iconMenuToolbar.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0 ){
                supportFragmentManager.popBackStack()
            } else
                onBackPressedDispatcher.onBackPressed()
        }

        if (savedInstanceState == null) {
            val fragment = FragmentTopUp()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_topUp, fragment)
                .commit()
        }
    }
}