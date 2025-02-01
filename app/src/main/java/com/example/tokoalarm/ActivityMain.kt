package com.example.tokoalarm

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ActivityMain : AppCompatActivity() {

    private var currentFragment: String? = null
    private var slide: Boolean = false

    private lateinit var session: Session
    private lateinit var prefManager: PrefManager

    private lateinit var btnHome: LinearLayout
    private lateinit var btnDevice: LinearLayout
    private lateinit var btnSchedule: LinearLayout
    private lateinit var btnAccount: LinearLayout

    private lateinit var iconHome: ImageView
    private lateinit var iconDevice: ImageView
    private lateinit var iconSchedule: ImageView
    private lateinit var iconAccount: ImageView

    private lateinit var textHome: TextView
    private lateinit var textDevice: TextView
    private lateinit var textSchedule: TextView
    private lateinit var textAccount: TextView

    private lateinit var dialogAlert: DialogAlert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefManager = PrefManager(this@ActivityMain)
        session = Session(prefManager)
        val utils = Utils(this@ActivityMain)
        val loading = DialogLoading(this)

        dialogAlert = DialogAlert(this)

        checkNotificationPermission()

        lifecycleScope.launch {
            try {
                loading.startLoadingDialog()
                val fromRegister = intent.getBooleanExtra("register", false)
                val fromLogin = intent.getBooleanExtra("login", false)
                if (fromRegister && session.getIdUser().isNullOrEmpty()) {
                    getUserId()

                } else if (fromLogin) {
                    utils.getBanner()
                }
                loading.dismissDialog()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnHome = findViewById(R.id.home_id)
        btnDevice = findViewById(R.id.device_id)
        btnSchedule = findViewById(R.id.schedule_id)
        btnAccount = findViewById(R.id.account_id)

        iconHome = findViewById(R.id.home_icon_id)
        iconDevice = findViewById(R.id.device_icon_id)
        iconSchedule = findViewById(R.id.schedule_icon_id)
        iconAccount = findViewById(R.id.account_icon_id)

        textHome = findViewById(R.id.home_text_id)
        textDevice = findViewById(R.id.device_text_id)
        textSchedule = findViewById(R.id.schedule_text_id)
        textAccount = findViewById(R.id.account_text_id)

        btnHome.setOnClickListener {
            updateSelectedMenu(btnHome)
            replaceFragment(FragmentHome(), getString(R.string.home))
        }
        btnDevice.setOnClickListener {
            updateSelectedMenu(btnDevice)
            replaceFragment(FragmentDevice(), getString(R.string.device))
        }
        btnSchedule.setOnClickListener {
            updateSelectedMenu(btnSchedule)
            replaceFragment(FragmentSchedule(), getString(R.string.schedule))
        }
        btnAccount.setOnClickListener {
            updateSelectedMenu(btnAccount)
            replaceFragment(FragmentAccount(), getString(R.string.account))
        }

        if (savedInstanceState == null) {
            val home = getString(R.string.home)
            updateSelectedMenu(btnHome)
            replaceFragment(FragmentHome(), home)
            currentFragment = home
        }
    }

    private fun checkNotificationPermission() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           val permission = android.Manifest.permission.POST_NOTIFICATIONS
           lifecycleScope.launch {
               if (ActivityCompat.checkSelfPermission(this@ActivityMain, permission) != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(this@ActivityMain, arrayOf(permission), 1)
                   if (prefManager.abortNotifFlow.first() == true) {
                       ifNotGranted()
                   }
               } else {
                   prefManager.setPermissionNotif(false)
               }
           }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (permissions[i] == android.Manifest.permission.POST_NOTIFICATIONS && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    lifecycleScope.launch {
                        prefManager.setPermissionNotif(false)
                        ifNotGranted()
                    }
                }
            }
        }
    }

    private fun ifNotGranted() {
        dialogAlert.show(getString(R.string.info),
            getString(R.string.notification_permission),
            R.raw.lottie_notif) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
            finish()
        }
    }


    private fun updateSelectedMenu(selectedBtn: LinearLayout) {
        btnHome.alpha = if (selectedBtn == btnHome) 1f else 0.5f
        btnDevice.alpha = if (selectedBtn == btnDevice) 1f else 0.5f
        btnSchedule.alpha = if (selectedBtn == btnSchedule) 1f else 0.5f
        btnAccount.alpha = if (selectedBtn == btnAccount) 1f else 0.5f
    }


    private fun replaceFragment(fragment: Fragment, tag: String) {
        if (tag == currentFragment) return

        val transaction = supportFragmentManager.beginTransaction()

        slide = when {
            tag == getString(R.string.home) -> true
            currentFragment == getString(R.string.home) -> false
            currentFragment == getString(R.string.device) && tag == getString(R.string.schedule) -> false
            currentFragment == getString(R.string.schedule) && tag == getString(R.string.device) -> true
            currentFragment == getString(R.string.account) && tag == getString(R.string.schedule) -> true
            currentFragment == getString(R.string.account) && tag == getString(R.string.device) -> true
            currentFragment == getString(R.string.device) && tag == getString(R.string.home) -> true
            currentFragment == getString(R.string.home) && tag == getString(R.string.device) -> false
            currentFragment == getString(R.string.account) && tag == getString(R.string.home) -> true
            currentFragment == getString(R.string.schedule) && tag == getString(R.string.home) -> true
            else -> false
        }

        if (slide) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        currentFragment = tag
    }

    private suspend fun getUserId() {
        val response = RetrofitClient.apiService.login(
            session.getPhone()!!,
            session.getPwd()!!
        )
        if (!response.isSuccessful) throw Exception("Response not successful")
        val loginResponse = response.body()
        if (loginResponse?.status != true) throw Exception("status not true")
        val data = loginResponse.data
        session.setIdUser(data.id)
    }

}