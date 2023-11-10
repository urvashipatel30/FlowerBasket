package com.flower.basket.orderflower.data

data class OrderResponse(
    val data: List<OrderData>,
    val succeeded: Boolean,
    val message: String = ""
)