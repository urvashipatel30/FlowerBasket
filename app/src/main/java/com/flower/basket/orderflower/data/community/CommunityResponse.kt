package com.flower.basket.orderflower.data.community

data class CommunityResponse(
    val data: List<CommunityData>,
    val succeeded: Boolean,
    val message: String = ""
)