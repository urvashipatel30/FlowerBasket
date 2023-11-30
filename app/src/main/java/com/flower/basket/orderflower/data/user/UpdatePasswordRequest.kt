package com.flower.basket.orderflower.data.user

data class UpdatePasswordRequest(
    val currentPassword: String,
    val password: String = ""
)