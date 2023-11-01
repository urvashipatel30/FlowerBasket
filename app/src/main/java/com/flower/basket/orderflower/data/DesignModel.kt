package com.flower.basket.orderflower.data

data class DesignModel (
    val id: String,
    val name: String,
    val note: String,
    val createdDate: String,
    val updatedDate: String,
    val createdBy: Int,
    val updatedBy: Int,
    val isArchived: Boolean
)