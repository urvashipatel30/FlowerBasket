package com.flower.basket.orderflower.ui.login.data

data class UserResponse(
    val data: UserData,
    val succeeded: Boolean,
    val message: String = ""
)