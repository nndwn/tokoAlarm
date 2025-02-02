package com.example.tokoalarm


import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header

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

    @FormUrlEncoded
    @POST("users/datapelanggan")
    suspend fun getDataPelanggan(
        @Field("id_users") idUser: String
    ): Response<DataPelangganResponse>

    @FormUrlEncoded
    @POST("users/historisaldo")
    suspend fun getHistorySaldo(
        @Field("id_users") idUser: String
    ): Response<TopUpResponse>

    @GET("users/paket")
    suspend fun getPaket () : Response<PaketResponse>

    @FormUrlEncoded
    @POST("sendMessage")
    suspend fun sendMessage(
        @Field("chat_id") chatId: String,
        @Field("text") text: String
    ): Response<String>

    @FormUrlEncoded
    @POST("users/belipaket")
    suspend fun beliPaket(
        @Field("nohp") nohp: String,
        @Field("periode") periode: String,
        @Field("day_convertion") dayConvertion: String,
        @Field("cutoff_day") cutoffDay: String,
        @Field("biaya") biaya: String,
        @Field("id_users") idUsers: String,
        @Field("id_alat") idAlat: String
    ) :Response<PaketResponse>

    @FormUrlEncoded
    @POST("users/getalat")
    suspend fun getAlat(
        @Field("nohp") nohp: String,
        @Field("status") status : String
    ) :Response<ListAlatResponse>


    @POST("alat/rename")
    fun renameAlat(
        @Header("x-api-key") apiKey: String,
        @Body jsonBody: JsonObject
    ): Call<JsonObject>
}

object RetrofitClient {
    private  val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val retrofitTelegram by lazy {
        Retrofit.Builder()
            .baseUrl(TELEGRAM_URL+ TELEGRAMTOKEN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}