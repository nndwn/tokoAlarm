package com.example.tokoalarm

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable


class DialogLoading(private val activity: Activity) {
    private lateinit var dialog: AlertDialog
    private var isShowing = false

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.activity_loading, null)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setOnDismissListener {
            isShowing = false
        }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        isShowing = true
    }
    fun dismissDialog() {
        dialog.dismiss()
        isShowing = false
    }
}