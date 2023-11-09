package com.flower.basket.orderflower.data

data class FlowerResponse(
    val data: List<FlowerData>,
    val succeeded: Boolean,
    val message: String = ""
)