package com.acm.newtokoalarm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Activity login handle two fragment
 * fragment login and fragment signUp
 * get intent data app url and save to viewModel
 */
class BActivityLogin :AppCompatActivity(){

    private val viewModel : BViewShared by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val dataUrl : ArrayList<AppUrl> = getParcelableList(intent, "dataUrl", AppUrl::class.java)
        viewModel.dataUrl.value = dataUrl

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, BFragmentLogin())
            .commit()

    }
}

class BViewShared() :ViewModel() {
    val dataUrl = MutableLiveData<List<AppUrl>>()
}
