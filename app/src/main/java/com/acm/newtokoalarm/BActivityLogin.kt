package com.acm.newtokoalarm

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Activity login handle two fragment
 * - fragment login and fragment signUp
 * - initiate viewModel
 * - if click Ui not field keyboard is gone
 * - get intent data app url and save to viewModel
 */
class BActivityLogin :AppCompatActivity(){

    private lateinit var viewModel : BViewShared
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this, BSharedViewModelFactory(this))[BViewShared::class.java]

        findViewById<ConstraintLayout>(R.id.root)
            .setOnClickListener {
                viewModel.utils.unfocus()
            }
        val dataUrl : ArrayList<AppUrl> = getParcelableList(intent, "dataUrl", AppUrl::class.java)
        viewModel.dataUrl.value = dataUrl

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, BFragmentLogin())
            .commit()

    }
}

class BViewShared(activity: Activity) :ViewModel() {
    val utils = GUtils(activity)
    val dialog = GDialog(activity)
    val dataUrl = MutableLiveData<List<AppUrl>>()
}
class BSharedViewModelFactory(private val activity: Activity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BViewShared::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BViewShared(activity) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}
