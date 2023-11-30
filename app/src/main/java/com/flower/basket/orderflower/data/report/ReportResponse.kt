package com.flower.basket.orderflower.data.report

data class ReportResponse(
    val data: List<ReportData>,
    val succeeded: Boolean,
    val message: String = ""
)