package com.example.tokoalarm

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView

class DialogError (private val activity: Activity){
    private var dialog: AlertDialog? = null
    private var isShowing = false

    fun startDialog(title: String, message: String) {
        if (!isShowing) {
            val builder = AlertDialog.Builder(activity)
            val myView = activity.layoutInflater.inflate(R.layout.activity_error, null)
            val tvTitle = myView.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = myView.findViewById<TextView>(R.id.tvMessage)
            val btnTutup = myView.findViewById<Button>(R.id.btnTutup)
            btnTutup.setText(R.string.close)
            btnTutup.setOnClickListener {
                isShowing = false
                dialog?.dismiss()
            }
            builder.setOnDismissListener {isShowing = false}
            tvTitle.text = title
            tvMessage.text = message
            builder.setView(myView)
            builder.setCancelable(true)

            dialog = builder.create()
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
            isShowing = true

        }
    }

    fun  dismissDialog() {
        isShowing = false
        dialog?.dismiss()
    }
}