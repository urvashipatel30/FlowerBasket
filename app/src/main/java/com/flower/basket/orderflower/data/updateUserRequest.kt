package com.flower.basket.orderflower.data

data class updateUserRequest(
    val userName: String,
    val profilePhoto: String = "",
    val mobileNumber: String,
    val communityId: Int,
    val block: String,
    val flatNo: String
)