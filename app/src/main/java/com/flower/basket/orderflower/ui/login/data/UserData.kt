package com.flower.basket.orderflower.ui.login.data

data class UserData(
    val id: String,
    val userType: Int = 0,
    val email: String,
    val userName: String,
    var password: String,
    val profilePhoto: String = "",
    val mobileNumber: String,
    val communityId: Int,
    val block: String,
    val flatNo: String,
    val authToken: String = "",
    val registeredDate: String = ""
)