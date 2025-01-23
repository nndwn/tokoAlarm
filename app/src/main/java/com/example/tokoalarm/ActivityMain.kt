package com.example.tokoalarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.google.android.material.bottomnavigation.BottomNavigationView


class ActivityMain :AppCompatActivity() {

    private var currentFragment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            val home = getString(R.string.home)
            replaceFragment(FragmentHome(), home)
            bottomNavigationView.selectedItemId = R.id.menu_home
            currentFragment = home

        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    replaceFragment(FragmentHome(), getString(R.string.home))
                    true
                }
                R.id.menu_device -> {
                    replaceFragment(FragmentDevice(), getString(R.string.device))
                    true
                }
                R.id.menu_schedule -> {
                    replaceFragment(FragmentSchedule(), getString(R.string.schedule))
                    true
                }
                R.id.menu_account -> {
                    replaceFragment(FragmentAccount(), getString(R.string.account))
                    true
                }
                else -> false
            }
        }
    }


    private fun replaceFragment(fragment: Fragment , tag :String) {

        if (tag == currentFragment) return
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        currentFragment = tag
    }
}