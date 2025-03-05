package com.acm.newtokoalarm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.jwt.JWT

class CActivityOtp :AppCompatActivity(){

    private lateinit var dialog: ZDialog
    private lateinit var utils: ZUtils
    private lateinit var field : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        dialog = ZDialog(this)
        utils = ZUtils(this)
        val dataOtp = intent.getStringExtra("OTP")
        field = findViewById(R.id.otp)

        findViewById<Button>(R.id.btnSend)
            .setOnClickListener {
                try {
                    val otpNumber = getOtp(dataOtp)
                    if (validate(otpNumber)){

                    }
                } catch (err : Exception) {
                    err.printStackTrace()
                    dialog.alert(
                        DialogData(
                            title = getString(R.string.error),
                            message = getString(R.string.appIssue),
                            animation = R.raw.error,
                            btnOne = getString(R.string.tutup)
                        )
                    ){ finish() }
                }
            }
        findViewById<Button>(R.id.btnBack)
            .setOnClickListener {
                dialog.comfirm(DialogData(
                    message = getString(R.string.guide_cancel_reg),
                    btnOne = getString(R.string.yes),
                    btnTwo = getString(R.string.no),
                    animation = R.raw.warn
                ), {
                    val intent = Intent(this, BActivityLogin::class.java)
                    startActivity(intent)
                    finish()
                })
            }
    }
    private fun validate( otp : Int) :Boolean {
        val input = field.text.toString()
        utils.unfocus()
        if (input.isEmpty()) {
            field.error = getString(R.string.inputEmpty)
            field.requestFocus()
            return false
        }
        if (input.toIntOrNull() != otp) {
            field.error = getString(R.string.otp_not_match)
            field.requestFocus()
            return false
        }
        return true
    }

    private fun getOtp (dataOtp: String?) :Int {
        if (dataOtp != null) {
            val jwt = JWT(dataOtp)
            val otp = jwt.getClaim("otp").asInt() ?: throw utils.error(Error.NULLEXCEPTION)
            return  otp
        }
        throw utils.error(Error.NULLEXCEPTION)
    }
}