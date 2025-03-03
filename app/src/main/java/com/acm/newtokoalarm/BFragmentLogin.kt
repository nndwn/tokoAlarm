package com.acm.newtokoalarm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels

class BFragmentLogin :Fragment(R.layout.fragment_login) {
    private lateinit var phoneNumber: EditText
    private lateinit var pwd : EditText
    private val sharedView: BSharredView by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phoneNumber = view.findViewById(R.id.phoneNumber)
        pwd = view.findViewById(R.id.password)

        view.findViewById<Button>(R.id.btnLogin)
            .setOnClickListener {
                if(validate()) {

                }
            }
        view.findViewById<Button>(R.id.btnDaftar)
            .setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .addToBackStack(null)
                    .replace(R.id.fragment, BFragmentSignUp())
                    .commit()
            }
    }
    private fun validate() :Boolean {
        val phone = phoneNumber.text
        val password = pwd.text
        val regex = Regex("^(\\+62|0)8[1-9][0-9]{10,11}$")
        if (phone.isEmpty()) {
            phoneNumber.error = getString(R.string.inputEmpty)
            phoneNumber.requestFocus()
            return false
        }
        if(!regex.matches(phone)) {
            phoneNumber.error = getString(R.string.noCorrectPhone)
            phoneNumber.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            pwd.error = getString(R.string.inputEmpty)
            pwd.requestFocus()
            return false
        }
        return true
    }
}