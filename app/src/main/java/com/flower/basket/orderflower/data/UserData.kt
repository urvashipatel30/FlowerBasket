package com.flower.basket.orderflower.data

data class UserData(
    val id: String,
    val userType: Int = 0,
    val email: String,
    val userName: String,
    val password: String,
    val profilePhoto: String = "",
    val mobileNumber: String,
    val communityId: Int,
    val block: String,
    val flatNo: String,
    val authToken: String = "",
    val registeredDate: String = ""
)