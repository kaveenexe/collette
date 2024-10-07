package com.example.colllette.model

data class Cart(
    val id: String,
    val userId: String,
    val items: MutableList<CartItem> = mutableListOf(),
    var totalPrice: Double = 0.0
)

data class CartItem(
    val productId: String,
    val productName: String,
    var quantity: Int,
    val price: Double
)