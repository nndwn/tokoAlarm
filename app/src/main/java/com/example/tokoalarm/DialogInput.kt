package com.example.tokoalarm

import android.app.Activity
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogInput(private val activity: Activity ) {

    private lateinit var sheetDialog: BottomSheetDialog
    private var isShowing = false
    var parent: ViewGroup? = null
    var title : String = ""
    var text : String = ""
    fun show(click : ()-> Unit = {}) {
        sheetDialog = BottomSheetDialog(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_input, parent, false)

        sheetDialog.setContentView(view)
        sheetDialog.setCancelable(true)
        sheetDialog.show()

        sheetDialog.setOnDismissListener {
            isShowing = false
        }
        view.findViewById<TextView>(R.id.title).text = title
        val editText = view.findViewById<EditText>(R.id.text)
        editText.setText(text)
        view.findViewById<TextView>(R.id.button).setOnClickListener {
            text = editText.text.toString()
            click()
            sheetDialog.dismiss()
        }
        isShowing = true
    }
}