package com.flower.basket.orderflower.data

data class DesignResponse (
    val data: List<DesignModel>,
    val succeeded: Boolean,
    val messages: String
)