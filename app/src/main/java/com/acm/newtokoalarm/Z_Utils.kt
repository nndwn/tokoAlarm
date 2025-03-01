package com.acm.newtokoalarm

import android.content.Context

enum class Error {
    UNSUCCESS,
    SERVERISSUE
}

class Z_Utils( private val context: Context) {
    private var _messageError = ""
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
        }
    }
}

