package com.acm.newtokoalarm

import android.content.Context
import androidx.lifecycle.ViewModel

enum class Error {
    UNSUCCESS,
    SERVERISSUE,
    NULLEXCEPTION
}

class GUtils(private val context: Context) {
    private var _messageError = context.getString(R.string.appIssue)
    private var _nameError = ""
    val nameError : String
        get() = _nameError
    val messageError :String
        get() = _messageError

    fun error(err: Error) {
        _nameError = err.name
        when (err) {
            Error.SERVERISSUE -> {
                val ctx = context.getString(R.string.serverissue)
                _messageError = ctx
                throw  Exception(ctx)
            }
            Error.UNSUCCESS -> {
                val ctx = context.getString(R.string.unsuccess)
                _messageError = ctx
                throw Exception(ctx)
            }
            Error.NULLEXCEPTION -> {
                _messageError =  context.getString(R.string.appIssue)
                throw Exception("Null Object")
            }
        }
    }
}

