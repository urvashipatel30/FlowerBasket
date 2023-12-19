package com.flower.basket.orderflower.data.user

data class TotalUsersResponse(
    val data: List<TotalUsersData>,
    val succeeded: Boolean,
    val message: String = ""
)