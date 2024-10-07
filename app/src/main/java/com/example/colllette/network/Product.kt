package com.example.colllette.model

data class Product(
    val id: String?,
    val uniqueProductId: String,
    val name: String,
    val description: String?,
    var category: String,
    val price: Double,
    val stockQuantity: Int,
    val vendorId: String,
    val isActive: Boolean
)