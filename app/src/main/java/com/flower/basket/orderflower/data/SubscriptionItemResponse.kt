package com.flower.basket.orderflower.data

data class SubscriptionItemResponse(
    val data: SubscriptionItemData,
    val succeeded: Boolean,
    val message: String = ""
)