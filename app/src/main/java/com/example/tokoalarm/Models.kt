package com.example.tokoalarm

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: UserData
)

data class RegisterResponse(
    val status: Boolean,
    val message: String,
    val data: UserData?
)

data class UserData (
    val id: String,
    val nama: String,
    val nohp: String,
    val password: String
)

data class DataPelangganResponse(
    val status :Boolean,
    val saldo :String,
    val data :List<ListPromo>,
    val config: Config
)

data class Config(
    @SerializedName("link_tutorial")
    val linkTutorial : String,
    @SerializedName("link_pesan_alarm")
    val linkPesanAlarm :String
)

data class ListPromo(
    @SerializedName("updated_at")
    val updatedAt :String,
    val banner :String,
    @SerializedName("created_at")
    val createdAt :String,
    val id :String,
    val deskripsi :String,
    val title :String
)