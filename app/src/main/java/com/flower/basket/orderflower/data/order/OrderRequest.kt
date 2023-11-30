package com.flower.basket.orderflower.data.order

import com.flower.basket.orderflower.utils.FlowerType
import com.flower.basket.orderflower.utils.SubscriptionType
import com.google.type.DateTime
import java.util.Date

data class OrderRequest(
    val userId: String,
    val flowerId: Int,
    val subscriptionType: Int = SubscriptionType.BuyOnce.value,
    val qty: Int = 1,
    val flowerType: Int = FlowerType.LOOSE_FLOWER.value,
    val interval: String = "",
    val subscriptionEndDate: String? = null
)