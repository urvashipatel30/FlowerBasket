package com.flower.basket.orderflower.data

data class UserData(
    val id: String = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
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