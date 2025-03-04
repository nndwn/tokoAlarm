package com.acm.newtokoalarm

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

/**
 * fragment sign up from fragment login with activity login
 * - first get data app url privacy from viewModel login
 * - show short text aggrement with direct link
 * - link to go activity web view
 * - validation diferent from login
 * - check not empty field
 * - check pattern name must alphabet
 * - check pattern phone must format indonesia phone
 * - check password character minimal 8 char
 * - check aggrement must checked
 */
class BFragmentSignUp :Fragment(R.layout.fragment_signup) {

    private lateinit var phoneNumber : EditText
    private lateinit var pwd : EditText
    private lateinit var name : EditText
    private lateinit var aggrement : CheckBox
    private lateinit var aggrementText: TextView

    private lateinit var viewModel : BViewShared

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[BViewShared::class.java]

        phoneNumber = view.findViewById(R.id.phoneNumber)
        pwd = view.findViewById(R.id.password)
        name = view.findViewById(R.id.nameAccount)
        aggrement = view.findViewById(R.id.aggrement_checkbox)
        aggrementText = view.findViewById(R.id.aggrement_text)

        aggrement.setOnClickListener{
            viewModel.utils.unfocus()
        }
        aggrementMore()

        view.findViewById<Button>(R.id.btnDaftar)
            .setOnClickListener {
                if(validate()) {

                }
            }
        view.findViewById<Button>(R.id.btnLogin)
            .setOnClickListener {
                parentFragmentManager.popBackStack()
            }
    }

    private fun aggrementMore() {
        viewModel.dataUrl.observe(viewLifecycleOwner) { data ->
            data.forEach{
                if (it.name == "privacy") {
                    val aggrementMoreText = SpannableStringBuilder()
                        .append(getString(R.string.aggrement_more))
                        .append(" ")
                        .append(getString(R.string.here)).apply {
                            val start = length - getString(R.string.here).length
                            setSpan(object : ClickableSpan() {
                                override fun onClick(widget: View) {
                                    val intent = Intent(activity, XActivityWeb::class.java)
                                    intent.putExtra("URL", it.url)
                                    startActivity(intent)
                                }
                            }, start, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            setSpan(ForegroundColorSpan(view?.context!!.getColor(R.color.blue)), start, length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                    aggrementText.text = aggrementMoreText
                    aggrementText.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        }
    }

    private fun validate () :Boolean {
        val nameText = name.text.toString().trim()
        val phone = phoneNumber.text.toString().trim()
        val password = pwd.text.toString()

        viewModel.utils.unfocus()
        name.clearFocus()
        pwd.clearFocus()
        phoneNumber.clearFocus()

        if (nameText.isEmpty()) {
            name.error = getString(R.string.inputEmpty)
            name.requestFocus()
            return false
        }
        if (!namePattern(nameText)){
            name.error = getString(R.string.invalidInput)
            name.requestFocus()
            return false
        }
        if (phone.isEmpty()) {
            phoneNumber.error = getString(R.string.inputEmpty)
            phoneNumber.requestFocus()
            return false
        }
        if (!phonePattern(phone)) {
            phoneNumber.error = getString(R.string.invalidInput)
            phoneNumber.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            pwd.error = getString(R.string.inputEmpty)
            pwd.requestFocus()
            return false
        }
        if (password.length < 8) {
            pwd.error = getString(R.string.password_minimal)
            pwd.requestFocus()
            return false
        }
        if (aggrement.isChecked.not()){
            val alert = DialogData(
                title = getString(R.string.error),
                message = getString(R.string.aggrement_check),
                animation = R.raw.error,
                btnText = getString(R.string.tutup)
            )
            viewModel.dialog.alert(alert)
            return false
        }


        return true
    }
}