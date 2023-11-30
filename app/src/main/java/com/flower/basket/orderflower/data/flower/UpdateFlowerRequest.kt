package com.flower.basket.orderflower.data.flower

data class UpdateFlowerRequest(
    val name: String,
    val teluguName: String,
    val loosePrice: Int,
    val moraPrice: Int,
    val imageUrl: String
)