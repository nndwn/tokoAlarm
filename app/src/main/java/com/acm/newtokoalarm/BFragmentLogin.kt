package com.acm.newtokoalarm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

/**
 * fragment login have 2 button
 * - if button login check validate
 * - check phone must format indonesia phone number
 * - check dont input empty value
 * - unfocus and clear focus if already type
 * - if valid to go loading dialog and get all data user
 * - if done sent intent data user to activity main
 * - button sign up to create fragment sign up and go
 */
class BFragmentLogin :Fragment(R.layout.fragment_login) {
    private lateinit var phoneNumber: EditText
    private lateinit var pwd : EditText

    private lateinit var viewModel: BViewShared

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[BViewShared::class.java]

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

        viewModel.utils.unfocus()

        phoneNumber.clearFocus()
        pwd.clearFocus()

        if (phone.isEmpty()) {
            phoneNumber.error = getString(R.string.inputEmpty)
            phoneNumber.requestFocus()
            return false
        }
        if(!phonePattern(phone)) {
            phoneNumber.error = getString(R.string.invalidInput)
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