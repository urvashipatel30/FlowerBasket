package com.flower.basket.orderflower.data.user

data class TotalUsersData(
    val id: String,
    val userType: Int,
    val email: String,
    val userName: String,
    val password: String,
    val profilePhoto: String,
    val mobileNumber: String,
    val communityId: Int,
    val block: String,
    val flatNo: String,
    val authToken: String? = null,
    val registeredDate: String
)