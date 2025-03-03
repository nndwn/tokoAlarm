package com.acm.newtokoalarm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

class BActivityLogin :AppCompatActivity(){
    private val sharedView: BSharredView by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, BFragmentLogin())
            .commit()
    }
}

class BSharredView : ViewModel() {
    fun test (){

    }
}