package com.flower.basket.orderflower.api.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//object RetroClient {
//    private const val BASE_URL = "https://12bb-123-201-29-17.ngrok-free.app/"
//    var mAPIService: ApiService? = null
//
//    val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//}
//
//object ApiClient {
//    val apiService: ApiService by lazy {
//        RetroClient.retrofit.create(ApiService::class.java)
//    }
//}

object RetroClient {
    private const val BASE_URL = "https://12bb-123-201-29-17.ngrok-free.app/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}