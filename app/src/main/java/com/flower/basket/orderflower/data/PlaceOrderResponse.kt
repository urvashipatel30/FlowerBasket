package com.flower.basket.orderflower.data

data class PlaceOrderResponse(
    val data: String,
    val succeeded: Boolean,
    val message: String = ""
)