package com.acm.newtokoalarm

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE

enum class Error {
    UNSUCCESS,
    SERVERISSUE,
    NULLEXCEPTION,
    CONNECTIONISSUE
}

fun phonePattern(phone : CharSequence) : Boolean {
    val regex = Regex("^(\\+62|62|0)8[1-9][0-9]{7,10}$")
    return regex.matches(phone)
}

fun namePattern(name : CharSequence) :Boolean {
    val regex = Regex("^[a-zA-Z\\s]+$")
    return regex.matches(name)
}

fun <T : Parcelable> getParcelableList(intent: Intent, key: String, clazz: Class<T>): java.util.ArrayList<T> {
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
       return intent.getParcelableArrayListExtra(key, clazz) ?: ArrayList()
    } else {
        @Suppress("DEPRECATION")
       return intent.getParcelableArrayListExtra<T>(key) ?: ArrayList()
    }
}

class GUtils(private val context: Activity) {
    private var _messageError = context.getString(R.string.appIssue)
    private var _nameError = ""
    val nameError : String
        get() = _nameError
    val messageError :String
        get() = _messageError

    fun unfocus () {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
    }

    fun error(err: Error) {
        _nameError = err.name
        when (err) {
            Error.SERVERISSUE -> {
                val ctx = context.getString(R.string.serverissue)
                _messageError = ctx
                throw  Exception(ctx)
            }
            Error.UNSUCCESS -> {
                val ctx = context.getString(R.string.connectionIssue)
                _messageError = ctx
                throw Exception(ctx)
            }
            Error.NULLEXCEPTION -> {
                _messageError =  context.getString(R.string.appIssue)
                throw Exception("Null Object")
            }
            Error.CONNECTIONISSUE -> {
                val ctx = context.getString(R.string.connectionIssue)
                _messageError = ctx
                throw Exception(ctx)
            }
        }
    }
}

