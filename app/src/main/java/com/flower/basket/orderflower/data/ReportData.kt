package com.flower.basket.orderflower.data

data class ReportData(
    val orderId: String,
    val subscriptionId: String,
    val flowerId: Int,
    val flowerName: String,
    val flowerTeluguName: String,
    val flowerImageUrl: String,
    val qty: Int,
    val price: Int,
    val totalPrice: Int,
    val flowerType: Int,
    val orderDate: String,
    val deliveryDate: String,
    var orderStatus: Int,
    val userId: String,
    val userName: String,
    val userEmail: String,
    val communityId: Int,
    val communityName: String,
    val mobileNumber: String,
    val block: String,
    val flatNo: String
)