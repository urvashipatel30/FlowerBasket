package com.flower.basket.orderflower.data.totalflowers

data class TotalFlowersData(
    val flowerId: Int,
    val flowerName: String,
    val flowerTeluguName: String,
    val flowerImageUrl: String,
    val totalLooseQty: Int,
    val totalMoraQty: Int
)