package com.acm.newtokoalarm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ResponseData (
    val code :Int,
    val message :String,
    val result: String
)

@Parcelize
data class ListTopUp (
    val harge :Int
):Parcelable

@Parcelize
data class Promo (
    val image :String,
    val link :String,
    val alt :String
):Parcelable

@Parcelize
data class Paket (
    val name: String,
    val durasi:Int,
    val harga:Int
):Parcelable

@Parcelize
data class AppUrl (
    val name :String,
    val url :String
):Parcelable

@Parcelize
data class AppChannel (
    val name :String
):Parcelable

@Parcelize
data class AppContact (
    val name :String,
    val email :String,
    val phone :String,
    val address : String
):Parcelable

@Parcelize
data class MethodPay (
    val name :String,
    val nomor :String,
    val owner :String
):Parcelable

@Parcelize
data class SignUp (
    val phone : String,
    val password : String,
    val name : String
):Parcelable
