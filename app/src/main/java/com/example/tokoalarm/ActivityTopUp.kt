package com.example.tokoalarm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class ActivityTopUp :AppCompatActivity(){
    private lateinit var viewModel: SharedViewTopUp
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_toolbar_fragment)

        viewModel = ViewModelProvider(this)[SharedViewTopUp::class.java]


        val titleToolbar = findViewById<TextView>(R.id.textMenu)
        titleToolbar.text = getString(R.string.isi_paket)
        findViewById<View>(R.id.toolbar).setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0 ){
                supportFragmentManager.popBackStack()
            } else
                onBackPressedDispatcher.onBackPressed()
        }

        if (savedInstanceState == null) {
            val fragment = FragmentTopUp()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit()
        }
    }
}