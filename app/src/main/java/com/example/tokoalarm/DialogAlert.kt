package com.example.tokoalarm

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class DialogAlert (
    private val activity : Activity
) {
    private lateinit var dialog : AlertDialog
    private var isShowing = false
    var title :String = ""
    var message :String = ""
    var animation : Int = 0
    var loop : Int = LottieDrawable.INFINITE
    var textBtn : String? = null
    fun show(
        click: () -> Unit = {}
    ) {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_error, null)
        builder.setView(view)
        builder.setCancelable(true)

        val fadeInScale = AnimationUtils.loadAnimation(activity, R.anim.fade_in_scale)
        view.startAnimation(fadeInScale)

        view.findViewById<TextView>(R.id.tvTitle).text = title
        view.findViewById<TextView>(R.id.tvMessage).text = message
        val icon  = view.findViewById<LottieAnimationView>(R.id.animation_view)
        icon.setAnimation(animation)
        icon.setRepeatCount(loop);

        if(textBtn != null) {
            view.findViewById<Button>(R.id.btnTutup).text = textBtn
        }

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

        view.findViewById<Button>(R.id.btnTutup).setOnClickListener {
            click()
            dismiss()
        }

        isShowing = true

    }
}