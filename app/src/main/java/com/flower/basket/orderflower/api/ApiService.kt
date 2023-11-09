package com.flower.basket.orderflower.api

import com.flower.basket.orderflower.data.CommunityResponse
import com.flower.basket.orderflower.data.FlowerResponse
import com.flower.basket.orderflower.data.LoginRequest
import com.flower.basket.orderflower.data.UserData
import com.flower.basket.orderflower.data.UserRequest
import com.flower.basket.orderflower.data.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("api/Community")
    fun getCommunities(): Call<CommunityResponse>

    @POST("api/Users/register")
    fun registerUser(@Body registerParams: UserRequest): Call<UserResponse>

    @POST("api/Users/login")
    fun loginUser(@Body loginParams: LoginRequest): Call<UserResponse>
    @GET("api/Flowers/GetAll")
    fun getFlowersList(): Call<FlowerResponse>
}