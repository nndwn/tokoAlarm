package com.acm.newtokoalarm

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide

data class DialogData(
    val title : String = "",
    val message : String = "",
    val animation : Int = 0,
    val loop : Int = LottieDrawable.INFINITE,
    val btnText : String = ""
)

class GDialog (private val activity : Activity) {

    private lateinit var loadingDialog: AlertDialog

    fun alert(settings : DialogData, callback: ()-> Unit ={}){
        val fadeInScale = AnimationUtils.loadAnimation(activity, R.anim.fade_in_scale)
        val fadeOutScale = AnimationUtils.loadAnimation(activity, R.anim.fade_out_scale)

        val view = activity.layoutInflater.inflate(R.layout.dialog_alert, null)
        view.startAnimation(fadeInScale)
        view.findViewById<TextView>(R.id.tvTitle)
            .text = settings.title
        view.findViewById<TextView>(R.id.tvMessage)
            .text = settings.message

        val icon = view.findViewById<LottieAnimationView>(R.id.animation_view)
        icon.setAnimation(settings.animation)
        icon.repeatCount = settings.loop

        val builder = AlertDialog.Builder(activity,R.style.CustomDialogTheme)
        builder.setView(view)

        val dialog = builder.create()
        dialog.setOnDismissListener {
            callback()
        }

        val btn = view.findViewById<Button>(R.id.btnTutup)
        btn.text = settings.btnText
        btn.setOnClickListener {
            view.startAnimation(fadeOutScale)
            view.postDelayed({
                dialog.dismiss()
            }, 300)
        }

        dialog.show()
    }

    fun loading (currentDim: Float = 0.7f, background :Boolean = false) {
        var dim = currentDim
        val animFadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        val animFadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
        val view = activity.layoutInflater.inflate(R.layout.dialog_loading, null)
        view.startAnimation(animFadeIn)
        val icon = view.findViewById<ImageView>(R.id.gif)
        val bg = view.findViewById<View>(R.id.background)
        if (background) {
            dim = 0f
            bg.visibility = View.VISIBLE
        }
        Glide.with(view.context)
            .asGif()
            .load(R.drawable.icon_animasi)
            .into(icon)

        val builder = AlertDialog.Builder(activity, R.style.CustomDialogTheme)
        builder.setView(view)

        loadingDialog = builder.create()
        loadingDialog.window?.setDimAmount(dim)

        loadingDialog.setOnDismissListener {
            view.startAnimation(animFadeOut)
        }
        loadingDialog.show()
    }

    fun dismissLoading() {
        if (this::loadingDialog.isInitialized) {
            loadingDialog.dismiss()
            return
        }
        throw Exception("Missing Function Loading Dialog")
    }

}