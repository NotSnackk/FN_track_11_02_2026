package com.example.fortnite_tracker.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://fortnite-api.com/"
    private const val API_KEY = "c25ac8f9-e43c-452e-9ad6-92b7e7a261f6"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", API_KEY)
                    .addHeader("x-api-key", API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    val retrofitService: FortniteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FortniteApiService::class.java)
    }
}
