package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ActivityLogin : AppCompatActivity(){
    private lateinit var phoneNumber: EditText
    private lateinit var pwdText: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button


    private  lateinit var loading: DialogLoading
    private lateinit var errorFail: DialogAlert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        phoneNumber = findViewById(R.id.phoneNumber)
        pwdText = findViewById(R.id.password)
        loginBtn = findViewById(R.id.btnLogin)
        registerBtn = findViewById(R.id.btnDaftar)

        loginBtn.setOnClickListener {
            if (validateInput()) {
                loginUser()
            }
            unFocus()
        }
        registerBtn.setOnClickListener {
            val intent = Intent(this@ActivityLogin, ActivityRegister::class.java)
            startActivity(intent)
            unFocus()
        }
    }

    private fun unFocus() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        phoneNumber.clearFocus()
        pwdText.clearFocus()
    }

    private fun loginUser() {
        loading = DialogLoading(this@ActivityLogin)
        errorFail = DialogAlert(this@ActivityLogin)

        loading.startLoadingDialog()

        lifecycleScope.launch {
            val response = RetrofitClient.apiService.login(
                phoneNumber.text.toString(),
                pwdText.text.toString()
            )

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse?.status == true) {
                    val data = loginResponse.data
                    val sessionLogin = PrefManager(applicationContext)
                    sessionLogin.setIdUser(data.id)
                    sessionLogin.setNameUser(data.nama)
                    sessionLogin.setPhone(data.nohp)
                    sessionLogin.setPwd(data.password)

                    val intent = Intent(this@ActivityLogin, ActivityMain::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("login", true)
                    startActivity(intent)
                    finish()
                } else {
                    errorFail.apply {
                        title = getString(R.string.fail_login)
                        message =  getString(R.string.fail_access)
                        animation = R.raw.crosserror
                    }.show ()
                }
            } else {
                errorFail.apply {
                    title = getString(R.string.fail_login)
                    message =  getString(R.string.trouble_connection)
                    animation = R.raw.crosserror
                }
            }
            loading.dismissDialog()

        }
    }

    private fun validateInput(): Boolean {
        val phone = phoneNumber.text.toString().trim()
        val password = pwdText.text.toString().trim()

        return  when {
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
            else -> true
        }
    }
}