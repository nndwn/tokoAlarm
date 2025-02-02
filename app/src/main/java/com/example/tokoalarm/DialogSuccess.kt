package com.example.tokoalarm

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class DialogSuccess( private  val activity : Activity ) {
  private lateinit var dialog : AlertDialog
  private var isShowing = false
    var title :String = ""
    var animation :Int = 0
    fun show (callback : () -> Unit = {}) {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_success , null)

        view.findViewById<LottieAnimationView>(R.id.animation_view)
            .setAnimation(animation)
        view.findViewById<TextView>(R.id.tvTitle).text = title

        builder.setView(view)
        builder.setCancelable(true)

        builder.setOnDismissListener {
            callback()
            isShowing = false
        }

        val fadeInScale = AnimationUtils.loadAnimation(activity, R.anim.fade_in_scale)
        view.startAnimation(fadeInScale)

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val fadeOutScale = AnimationUtils.loadAnimation(activity, R.anim.fade_out_scale)
        view.startAnimation(fadeOutScale)
        view.postDelayed({
            dialog.dismiss()
        }, 2000)


    }
}