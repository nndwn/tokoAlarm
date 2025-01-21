package com.example.tokoalarm



data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: UserData
)

data class UserData (
    val id: String,
    val nama: String,
    val nohp: String,
    val password: String
)