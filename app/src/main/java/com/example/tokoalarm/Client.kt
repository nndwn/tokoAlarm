package com.example.tokoalarm


import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded

interface ApiService {
    @FormUrlEncoded
    @POST("users/auth")
    suspend fun login(
        @Field("nohp") phone: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("users/regis")
    suspend fun sigUp(
        @Field("nama") name: String,
        @Field("nohp") phone: String,
        @Field("password") password: String
    ): Response<RegisterResponse>
}


object RetrofitClient {
    private  val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}