package com.flower.basket.orderflower.data

import java.io.Serializable

data class FlowerData(
    val id: Int,
    var name: String,
    var teluguName: String,
    var loosePrice: Int,
    var moraPrice: Int,
    val imageUrl: String,
    val createdDate: String
) : Serializable