package com.flower.basket.orderflower.api

import com.flower.basket.orderflower.data.CommunityResponse
import com.flower.basket.orderflower.data.DeleteSubscriptionResponse
import com.flower.basket.orderflower.data.FlowerResponse
import com.flower.basket.orderflower.data.LoginRequest
import com.flower.basket.orderflower.data.OrderRequest
import com.flower.basket.orderflower.data.OrderResponse
import com.flower.basket.orderflower.data.PlaceOrderResponse
import com.flower.basket.orderflower.data.SubscriptionItemResponse
import com.flower.basket.orderflower.data.SubscriptionListResponse
import com.flower.basket.orderflower.data.UpdateSubscriptionRequest
import com.flower.basket.orderflower.data.UpdateSubscriptionResponse
import com.flower.basket.orderflower.data.UserRequest
import com.flower.basket.orderflower.data.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("api/Community/GetAll")
    fun getCommunities(): Call<CommunityResponse>

    @POST("api/Users/register")
    fun registerUser(@Body registerParams: UserRequest): Call<UserResponse>

    @POST("api/Users/login")
    fun loginUser(@Body loginParams: LoginRequest): Call<UserResponse>

    @GET("api/Flowers/GetAll")
    fun getFlowersList(): Call<FlowerResponse>

    //Place Order
    @POST("/api/Subscriptions/Add")
    fun placeOrder(@Body orderParams: OrderRequest): Call<PlaceOrderResponse>

    //Subscription List
    @GET("/api/Subscriptions/GetAll/{id}")
    fun getAllSubscriptions(@Path("id") userId: String): Call<SubscriptionListResponse>

    //Subscription Item Detail
    @GET("/api/Subscriptions/Get/{id}")
    fun getSubscriptionsDetail(@Path("id") subscriptionId: String): Call<SubscriptionItemResponse>

    //Update Subscription
    @PUT("/api/Subscriptions/Update/{id}")
    fun updateSubscription(
        @Path("id") subscriptionId: String,
        @Body subscriptionsParams: UpdateSubscriptionRequest
    ): Call<UpdateSubscriptionResponse>

    //Delete Subscription
    @DELETE("/api/Subscriptions/Delete/{id}")
    fun deleteSubscription(@Path("id") subscriptionId: String): Call<DeleteSubscriptionResponse>

    //Order List
    @GET("/api/Order/GetAll/{id}")
    fun getAllOrders(@Path("id") userId: String): Call<OrderResponse>
}