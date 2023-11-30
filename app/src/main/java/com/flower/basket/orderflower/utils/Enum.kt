package com.flower.basket.orderflower.utils

enum class UserType(var value: Int) {
    User(0),
    Vendor(1)
}

enum class SubscriptionType(var value: Int) {
    BuyOnce(0),
    Subscribe(1)
}

enum class Quantity(var value: Int) {
    GRAMS(100),
    MORA(1)
}

enum class FlowerType(var value: Int) {
    LOOSE_FLOWER(0),
    MORA(1)
}

enum class OrderStatus(var value: Int) {
    PENDING(0),
    DELIVERED(1),
    CANCELED(2),
    IN_DELIVERY(3)
}