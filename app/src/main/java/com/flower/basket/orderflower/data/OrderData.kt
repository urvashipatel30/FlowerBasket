package com.flower.basket.orderflower.data

data class OrderData(
    val id: String,
    val subscriptionId: String,
    val userId: String,
    val userName: String,
    val flowerId: Int,
    val flowerName: String,
    val flowerTeluguName: String,
    val flowerImageUrl: String,
    var qty: Int,
    val price: Int,
    val totalPrice: Int,
    val flowerType: Int,
    val orderDate: String,
    val deliveryDate: String,
    val orderStatus: Int,
)