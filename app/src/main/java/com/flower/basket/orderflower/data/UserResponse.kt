package com.flower.basket.orderflower.data

data class UserResponse(
    val data: UserData,
    val succeeded: Boolean,
    val message: String = ""
)