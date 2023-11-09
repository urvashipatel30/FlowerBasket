package com.flower.basket.orderflower.data

import java.io.Serializable

data class FlowerData(
    val id: Int,
    val name: String,
    val teluguName: String,
    val loosePrice: Int,
    val moraPrice: Int,
    val imageUrl: String,
    val createdDate: String
) : Serializable