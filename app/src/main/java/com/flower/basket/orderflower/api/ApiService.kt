package com.flower.basket.orderflower.api

import com.flower.basket.orderflower.data.APIResponse
import com.flower.basket.orderflower.data.community.CommunityResponse
import com.flower.basket.orderflower.data.flower.FlowerResponse
import com.flower.basket.orderflower.data.flower.UpdateFlowerRequest
import com.flower.basket.orderflower.data.order.ChangeOrderStatusRequest
import com.flower.basket.orderflower.data.order.OrderRequest
import com.flower.basket.orderflower.data.order.OrderResponse
import com.flower.basket.orderflower.data.report.ReportResponse
import com.flower.basket.orderflower.data.subscription.SubscriptionItemResponse
import com.flower.basket.orderflower.data.subscription.SubscriptionListResponse
import com.flower.basket.orderflower.data.subscription.SubscriptionStatusRequest
import com.flower.basket.orderflower.data.subscription.UpdateSubscriptionRequest
import com.flower.basket.orderflower.data.totalflowers.TotalFlowersResponse
import com.flower.basket.orderflower.data.user.LoginRequest
import com.flower.basket.orderflower.data.user.RegistrationRequest
import com.flower.basket.orderflower.data.user.TotalUsersResponse
import com.flower.basket.orderflower.data.user.UpdatePasswordRequest
import com.flower.basket.orderflower.data.user.UpdateUserRequest
import com.flower.basket.orderflower.data.user.UserResponse
import com.flower.basket.orderflower.data.vendor.VendorContactResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("api/Community/GetAll")
    fun getCommunities(): Call<CommunityResponse>

    @POST("api/Users/register")
    fun registerUser(@Body registerParams: RegistrationRequest): Call<UserResponse>

    @POST("api/Users/login")
    fun loginUser(@Body loginParams: LoginRequest): Call<UserResponse>

    @GET("api/Users/GetAll")
    fun getUsersList(): Call<TotalUsersResponse>

    @PUT("api/Users/Update/{id}")
    fun updateUser(
        @Path("id") userId: String,
        @Body params: UpdateUserRequest
    ): Call<UserResponse>

    @PUT("api/Users/ChangePassword/{id}")
    fun changePassword(
        @Path("id") userId: String,
        @Body params: UpdatePasswordRequest
    ): Call<APIResponse>

    @GET("api/Flowers/GetAll")
    fun getFlowersList(): Call<FlowerResponse>

    //Place Order
    @POST("/api/Subscriptions/Add")
    fun placeOrder(@Body orderParams: OrderRequest): Call<APIResponse>

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
    ): Call<APIResponse>

    //Active/Deactive Subscription
    @PUT("/api/Subscriptions/ManageVacationMode/{id}")
    fun changeSubscriptionStatus(
        @Path("id") subscriptionId: String,
        @Body statusParams: SubscriptionStatusRequest
    ): Call<APIResponse>

    //Delete Subscription
    @DELETE("/api/Subscriptions/Delete/{id}")
    fun deleteSubscription(@Path("id") subscriptionId: String): Call<APIResponse>

    //Order List
    @GET("/api/Order/GetAll/{id}")
    fun getAllOrders(@Path("id") userId: String): Call<OrderResponse>

    //Generate Orders
    @POST("/api/Order/GenerateOrder/")
    fun generateOrders(): Call<APIResponse>

    //Cancel Order & Delivered Order
    @PUT("/api/Order/UpdateOrderStatus/{id}")
    fun changeOrderStatus(
        @Path("id") orderId: String,
        @Body params: ChangeOrderStatusRequest
    ): Call<APIResponse>

    //Get Vendor contact
    @GET("/api/Vendor/GetVendorByCommunity/{id}")
    fun getVendorContact(@Path("id") id: Int): Call<VendorContactResponse>


    //Update Flower Details
    @PUT("api/Flowers/Update/{id}")
    fun updateFlower(
        @Path("id") id: Int,
        @Body params: UpdateFlowerRequest
    ): Call<APIResponse>

    //Report List for vendor
    @GET("/api/Vendor/GetAllOrders/{communityId}/")
    fun getReport(
        @Path("communityId") id: Int,
        @Query("dateTime") date: String
    ): Call<ReportResponse>

    //Total Flowers List for vendor
    @GET("/api/Vendor/GetTotalFlowers/{communityId}/")
    fun getTotalFlowers(
        @Path("communityId") id: Int,
        @Query("dateTime") date: String
    ): Call<TotalFlowersResponse>
}