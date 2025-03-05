package com.acm.newtokoalarm

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("get")
    suspend fun getDataApp(): retrofit2.Response<ResponseData>
    @POST("reg/try")
    suspend fun getRegTry(
        @Body signUp: SignUp
    ): retrofit2.Response<ResponseData>

}

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("x-api-key", apiKey)
            .build()
        return chain.proceed(newRequest)
    }
}

object RetrofitClient {
    fun create(context: Context): ApiService {
        val baseUrl = context.getString(R.string.baseApi)
        val apiKey = context.getString(R.string.appkey)
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(apiKey))
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}