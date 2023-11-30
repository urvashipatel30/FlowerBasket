package com.flower.basket.orderflower.data.user

data class RegistrationRequest(
    val userType: Int = 0,
    val email: String,
    val userName: String,
    val password: String,
    val profilePhoto: String = "",
    val mobileNumber: String,
    val communityId: Int,
    val block: String,
    val flatNo: String
)