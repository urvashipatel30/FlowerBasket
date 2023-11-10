package com.flower.basket.orderflower.data

import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.SubscriptionType

data class UpdateSubscriptionRequest(
    val qty: Int,
    val interval: String
)