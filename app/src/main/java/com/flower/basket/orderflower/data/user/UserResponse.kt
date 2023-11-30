package com.flower.basket.orderflower.data.user

data class UserResponse(
    val data: UserData,
    val succeeded: Boolean,
    val message: String = ""
)