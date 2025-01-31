package com.example.tokoalarm

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView

class DialogConfirm (private val activty :Activity){
    private var dialog : AlertDialog? = null
    private var isShowing = false
    fun startDialog(message: String, onOk: () -> Unit) {
        if (!isShowing) {
            val builder = AlertDialog.Builder(activty)
            val myView = activty.layoutInflater.inflate(R.layout.dialog_confirm, null)
            val messageView = myView.findViewById<TextView>(R.id.tvMessage)
            val btnOk = myView.findViewById<TextView>(R.id.btnOkConfirm)
            val btnCancel = myView.findViewById<TextView>(R.id.btnCancelConfirm)
            messageView.text = message
            btnOk.setOnClickListener {
                onOk()
                isShowing = false
                dialog?.dismiss()
            }
            btnCancel.setOnClickListener {
                isShowing = false
                dialog?.dismiss()
            }

            builder.setOnDismissListener {
                isShowing = false
            }
            builder.setView(myView)
            builder.setCancelable(true)
            dialog = builder.create()
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
            isShowing = true
        }
    }
}