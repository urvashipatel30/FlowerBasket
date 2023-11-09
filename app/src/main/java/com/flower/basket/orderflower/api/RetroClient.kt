package com.flower.basket.orderflower.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroClient {
//    private const val BASE_URL = "https://flowerbasket.bsite.net/"
    private const val BASE_URL = "http://www.flowerbasket.somee.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}