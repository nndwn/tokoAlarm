package com.example.tokoalarm

import android.content.Intent
import android.net.Uri
import android.os.Build
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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Register :AppCompatActivity(){

    private lateinit var nameText: EditText
    private lateinit var phoneNumber: EditText
    private  lateinit var pwdText: EditText
    private lateinit var signUpBtn: Button
    private lateinit var aggrementCheck: CheckBox
    private lateinit var aggrementText: TextView

    private lateinit var errorDialog: ErrorDialog
    private  lateinit var loading: Loading


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        errorDialog = ErrorDialog(this@Register)
        aggrementText = findViewById(R.id.aggrement_text)
        aggrementMore()

        nameText = findViewById(R.id.reg_name)
        phoneNumber = findViewById(R.id.reg_phoneNumber)
        pwdText = findViewById(R.id.reg_password)
        signUpBtn = findViewById(R.id.reg_btn)
        aggrementCheck = findViewById(R.id.aggrement_checkbox)

        signUpBtn.setOnClickListener {
            if (validateInput()) {
                signUpUser()
            }
        }
    }


    private fun aggrementMore() {
        val readMoreText = SpannableStringBuilder()
            .append(getString(R.string.agreement_text))
            .append(" ")
            .append(getString(R.string.here)).apply {
                setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val url = "https://tokoalarm.com/syarat-ketentuan/"
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    }
                }, length - getString(R.string.here).length, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setSpan(ForegroundColorSpan(getColor(R.color.primary_color)), length - getString(R.string.here).length, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

        aggrementText.text = readMoreText
        aggrementText.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun validateInput(): Boolean {
        val name = nameText.text.toString()
        val phone = phoneNumber.text.toString().trim()
        val password = pwdText.text.toString().trim()
        val aggrement = aggrementCheck.isChecked

        return when {
            phone.isEmpty() -> {
                phoneNumber.error = getString(R.string.error_phone)
                phoneNumber.requestFocus()
                false
            }
            password.isEmpty() -> {
                pwdText.error = getString(R.string.error_password)
                pwdText.requestFocus()
                false
            }
            name.isEmpty() -> {
                nameText.error = getString(R.string.error_name)
                nameText.requestFocus()
                false
            }
            aggrement.not() -> {
                errorDialog.startDialog(getString(R.string.info), getString(R.string.error_aggrement))
                false
            }
            else -> true
        }
    }

    private fun signUpUser() {
        loading = Loading(this@Register)
        loading.startLoadingDialog()
        val failSignUp = getString(R.string.fail_register)
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.sigUp(
                    nameText.text.toString(),
                    phoneNumber.text.toString(),
                    pwdText.text.toString()
                )
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse?.status == true) {
                        val data = signUpResponse.data!!
                        val pref = PreferencesManager(applicationContext)
                        val sessionLogin = Session(pref)
                        sessionLogin.setIdUser(data.id)
                        sessionLogin.setNameUser(data.nama)
                        sessionLogin.setPhone(data.nohp)
                        sessionLogin.setPwd(data.password)
                        val intent = Intent(this@Register, Home::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        errorDialog.startDialog(failSignUp, signUpResponse?.message ?: getString(R.string.fail_register))
                    }
                } else {
                    errorDialog.startDialog(failSignUp, getString(R.string.fail_register))
                }

            } catch (e : Exception) {
                errorDialog.startDialog(failSignUp,getString(R.string.trouble_connection))
            } finally {
                loading.dismissDialog()
            }
        }
    }
}