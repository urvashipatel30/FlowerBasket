package com.flower.basket.orderflower.data

data class ReportDataToSend(
    val flowerName: String,
    val flowerTeluguName: String,
    val qty: Int,
    val userName: String,
    val price: Int,
    val totalPrice: Int,
    val flowerType: String,
//    val flowerImageUrl: String,
//    val orderDate: String,
//    val deliveryDate: String,
    val orderStatus: String
)