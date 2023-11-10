package com.flower.basket.orderflower.data

data class SubscriptionListData(
    val id: String,
    val userId: String,
    val userName: String,
    val flowerId: Int,
    val flowerName: String,
    val flowerType: Int,
    val flowerTeluguName: String,
    val flowerImageUrl: String,
    val subscriptionType: Int,
    val qty: Int,
    val loosePrice: Int,
    val moraPrice: Int,
    val interval: String,
    val subscriptionStartDate: String,
    val subscriptionEndDate: String? = null,
)