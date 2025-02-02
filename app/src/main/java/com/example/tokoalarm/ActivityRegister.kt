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
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class       ActivityRegister :AppCompatActivity(){

    private lateinit var nameText: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var pwdText: EditText
    private lateinit var signUpBtn: Button
    private lateinit var aggrementCheck: CheckBox
    private lateinit var aggrementText: TextView
    private lateinit var loginBtn: Button

    private  lateinit var loading: DialogLoading
    private  lateinit var alert : DialogAlert


    private var name :String? = null
    private var phone :String? = null
    private var password :String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        alert = DialogAlert(this)
        aggrementText = findViewById(R.id.aggrement_text)
        loginBtn = findViewById(R.id.btnLogin)
        loginBtn.setOnClickListener {
            val intent = Intent(this@ActivityRegister, ActivityLogin::class.java)
            startActivity(intent)
            unFocus()
        }

        aggrementMore()

        nameText = findViewById(R.id.reg_name)
        phoneNumber = findViewById(R.id.reg_phoneNumber)
        pwdText = findViewById(R.id.reg_password)
        signUpBtn = findViewById(R.id.reg_btn)
        aggrementCheck = findViewById(R.id.aggrement_checkbox)

        aggrementCheck.setOnClickListener {
            unFocus()
        }
        signUpBtn.setOnClickListener {
            name = nameText.text.toString()
            phone = phoneNumber.text.toString().trim()
            password = pwdText.text.toString().trim()

            if (validateInput()) {
                signUpUser()
            }
            unFocus()
        }
    }
    private fun unFocus() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        nameText.clearFocus()
        phoneNumber.clearFocus()
        pwdText.clearFocus()
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

        val aggrement = aggrementCheck.isChecked

        return when {
            phone!!.isEmpty() -> {
                phoneNumber.error = getString(R.string.error_phone)
                phoneNumber.requestFocus()
                false
            }
            password!!.isEmpty() -> {
                pwdText.error = getString(R.string.error_password)
                pwdText.requestFocus()
                false
            }
            name!!.isEmpty() -> {
                nameText.error = getString(R.string.error_name)
                nameText.requestFocus()
                false
            }
            aggrement.not() -> {
                alert.show(
                    getString(R.string.info),
                    getString(R.string.error_aggrement),
                    R.raw.crosserror )
                false
            }
            else -> true
        }
    }

    private fun signUpUser() {
        loading = DialogLoading(this@ActivityRegister)
        loading.startLoadingDialog()
        val failSignUp = getString(R.string.fail_register)
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.sigUp(
                    name!!,
                    phone!!,
                    password!!
                )
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse?.status == true) {
                        val pref = PrefManager(applicationContext)
                        val sessionLogin = Session(pref)
                        sessionLogin.setNameUser(name!!)
                        sessionLogin.setPhone(phone!!)
                        sessionLogin.setPwd(password!!)
                        val intent = Intent(this@ActivityRegister, ActivityMain::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("register", true)
                        startActivity(intent) 
                        finish()
                    } else {
                        alert.show(
                            failSignUp,
                            getString(R.string.fail_register),
                            R.raw.crosserror
                        )
                    }
                } else {
                    alert.show(
                        failSignUp,
                        getString(R.string.fail_register),
                        R.raw.crosserror)
                }

            } catch (e : Exception) {
                e.printStackTrace()
                alert.show(
                    failSignUp,
                    getString(R.string.trouble_connection),
                    R.raw.crosserror)
            } finally {
                loading.dismissDialog()
            }
        }
    }
}