package com.flower.basket.orderflower.data.vendor

data class VendorContactData(
    val id: String,
    val email: String,
    val userName: String,
    val profilePhoto: String = "",
    val mobileNumber: String,
    val communityId: Int,
    val communityName: String
)