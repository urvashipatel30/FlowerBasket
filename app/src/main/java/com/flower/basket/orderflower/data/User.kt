package com.flower.basket.orderflower.data

data class User(
    val userId: String,
    val userType: Int,
    val name: String,
    val email: String,
    val mobileNumber: String,
    val community: String,
    val block: String,
    val flat: String,
    val activationDate: String
)