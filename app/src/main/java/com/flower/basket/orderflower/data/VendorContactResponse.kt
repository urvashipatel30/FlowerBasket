package com.flower.basket.orderflower.data

data class VendorContactResponse(
    val data: VendorContactData,
    val succeeded: Boolean,
    val message: String = ""
)