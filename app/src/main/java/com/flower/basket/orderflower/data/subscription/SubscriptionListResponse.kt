package com.flower.basket.orderflower.data.subscription

data class SubscriptionListResponse(
    val data: List<SubscriptionListData>,
    val succeeded: Boolean,
    val message: String = ""
)