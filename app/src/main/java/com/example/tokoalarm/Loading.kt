package com.example.tokoalarm

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable


class Loading(private val activity: Activity) {
    private var dialog: AlertDialog? = null
    private var isShowing = false

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.loading, null)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setOnDismissListener {
            isShowing = false
        }

        dialog = builder.create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
        isShowing = true
    }
    fun dismissDialog() {
        dialog?.dismiss()
        isShowing = false
    }
}