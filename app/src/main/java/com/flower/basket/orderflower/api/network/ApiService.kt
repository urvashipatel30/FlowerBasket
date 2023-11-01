package com.flower.basket.orderflower.api.network

import com.flower.basket.orderflower.data.DesignResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/Designs")
    suspend fun getDesigns(): Response<DesignResponse>

    @GET("api/Designs")
    fun getPostById(): Call<DesignResponse>
}