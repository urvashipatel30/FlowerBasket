package com.flower.basket.orderflower.data.totalflowers

data class TotalFlowersResponse(
    val data: List<TotalFlowersData>,
    val succeeded: Boolean,
    val message: String = ""
)