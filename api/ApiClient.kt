package com.vibelocal.app.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Physical phone: use your computer's local IP address (found via ipconfig)
    private const val BASE_URL = "http://10.0.0.4:5000/"
    fun service(context: Context): ApiService {
        val log = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val auth = okhttp3.Interceptor { chain ->
            val token = context.getSharedPreferences("vibelocal_session", Context.MODE_PRIVATE).getString("token", null)
            val request = chain.request().newBuilder().apply { if (!token.isNullOrBlank()) addHeader("Authorization", "Bearer $token") }.build()
            chain.proceed(request)
        }
        val client = OkHttpClient.Builder().addInterceptor(auth).addInterceptor(log).build()
        return Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(ApiService::class.java)
    }
}
