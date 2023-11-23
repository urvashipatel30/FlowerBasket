package com.flower.basket.orderflower.data

data class UpdatePasswordRequest(
    val currentPassword: String,
    val password: String = ""
)