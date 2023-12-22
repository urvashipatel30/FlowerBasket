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
    @GET
    fun getCommunities(@Url url: String): Call<CommunityResponse>

    @POST
    fun registerUser(
        @Url url: String,
        @Body registerParams: RegistrationRequest
    ): Call<UserResponse>

    @POST
    fun loginUser(@Url url: String, @Body loginParams: LoginRequest): Call<UserResponse>

    @GET
    fun getUsersList(@Url url: String): Call<TotalUsersResponse>

    @PUT
    fun updateUser(
        @Url url: String,
        @Body params: UpdateUserRequest
    ): Call<UserResponse>

    @PUT
    fun changePassword(
        @Url url: String,
        @Body params: UpdatePasswordRequest
    ): Call<APIResponse>


    @GET
    fun deleteUser(@Url url: String): Call<APIResponse>

    @GET
    fun getFlowersList(@Url url: String): Call<FlowerResponse>

    //Place Order
    @POST
    fun placeOrder(@Url url: String, @Body orderParams: OrderRequest): Call<APIResponse>

    //Subscription List
    @GET
    fun getAllSubscriptions(@Url url: String): Call<SubscriptionListResponse>

    //Subscription Item Detail
    @GET
    fun getSubscriptionsDetail(@Url url: String): Call<SubscriptionItemResponse>

    //Update Subscription
    @PUT
    fun updateSubscription(
        @Url url: String,
        @Body subscriptionsParams: UpdateSubscriptionRequest
    ): Call<APIResponse>

    //Active/Deactive Subscription
    @PUT
    fun changeSubscriptionStatus(
        @Url url: String,
        @Body statusParams: SubscriptionStatusRequest
    ): Call<APIResponse>

    //Delete Subscription
    @DELETE
    fun deleteSubscription(@Url url: String): Call<APIResponse>

    //Order List
    @GET
    fun getAllOrders(@Url url: String): Call<OrderResponse>

    //Generate Orders
    @POST
    fun generateOrders(@Url url: String): Call<APIResponse>

    //Cancel Order & Delivered Order
    @PUT
    fun changeOrderStatus(
        @Url url: String,
        @Body params: ChangeOrderStatusRequest
    ): Call<APIResponse>

    //Get Vendor contact
    @GET
    fun getVendorContact(@Url url: String): Call<VendorContactResponse>


    //Update Flower Details
    @PUT
    fun updateFlower(
        @Url url: String,
        @Body params: UpdateFlowerRequest
    ): Call<APIResponse>


    //Report List for vendor
    @GET
    fun getReport(
        @Url url: String,
        @Query("dateTime") date: String
    ): Call<ReportResponse>


    //Total Flowers List for vendor
    @GET
    fun getTotalFlowers(
        @Url url: String,
        @Query("dateTime") date: String
    ): Call<TotalFlowersResponse>
}