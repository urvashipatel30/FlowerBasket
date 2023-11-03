package com.flower.basket.orderflower.data

data class CommunityResponse(
    val data: List<CommunityData>,
    val succeeded: Boolean,
    val message: String = ""
)