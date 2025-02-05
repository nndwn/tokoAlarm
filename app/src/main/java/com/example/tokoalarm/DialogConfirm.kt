package com.example.tokoalarm

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class DialogConfirm(
    private val activity: Activity
) {
    private lateinit var dialog: AlertDialog
    private var isShowing = false

    fun show(
        message: String,
        animation: Int,
        onOk: () -> Unit = {},
        onCancel: () -> Unit? = {}
    ) {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_confirm, null)
        builder.setView(view)
        builder.setCancelable(true)

        val fadeInScale = AnimationUtils.loadAnimation(activity, R.anim.fade_in_scale)
        view.startAnimation(fadeInScale)
        view.findViewById<TextView>(R.id.tvMessage).text = message
        view.findViewById<LottieAnimationView>(R.id.animation_view)
            .setAnimation(animation)

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


        fun dismiss() {
            val fadeOutScale = AnimationUtils.loadAnimation(activity, R.anim.fade_out_scale)
            view.startAnimation(fadeOutScale)
            view.postDelayed({
                dialog.dismiss()
            }, 300)
        }

        builder.setOnDismissListener {
            isShowing = false
        }

        view.findViewById<TextView>(R.id.btnOkConfirm)
            .setOnClickListener {
                onOk()
                dismiss()
            }

        view.findViewById<TextView>(R.id.btnCancelConfirm)
            .setOnClickListener {
                onCancel()
                dismiss()
            }

        isShowing = true
    }

}