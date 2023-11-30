package com.flower.basket.orderflower.data.flower

data class FlowerResponse(
    val data: List<FlowerData>,
    val succeeded: Boolean,
    val message: String = ""
)