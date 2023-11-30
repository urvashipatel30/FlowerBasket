package com.flower.basket.orderflower.data.order

data class OrderResponse(
    val data: List<OrderData>,
    val succeeded: Boolean,
    val message: String = ""
)