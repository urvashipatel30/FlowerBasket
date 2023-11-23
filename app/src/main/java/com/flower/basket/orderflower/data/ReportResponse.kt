package com.flower.basket.orderflower.data

data class ReportResponse(
    val data: List<ReportData>,
    val succeeded: Boolean,
    val message: String = ""
)