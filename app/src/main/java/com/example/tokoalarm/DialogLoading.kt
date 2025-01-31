package com.example.tokoalarm

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.AnimationUtils


class DialogLoading(private val activity: Activity) {
    private lateinit var dialog: AlertDialog
    private var isShowing = false

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setOnDismissListener {
            isShowing = false
        }
        val fadeInScale = AnimationUtils.loadAnimation(activity, R.anim.fade_in_scale)
        view.startAnimation(fadeInScale)

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