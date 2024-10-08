package com.example.colllette.model

data class Order(
    val id: String,
    val orderId: String,
    val status: String,
    val orderDate: String,
    val paymentMethod: String,
    val orderItemsGroups: List<OrderItemGroup>,
    val totalAmount: Double,
    val customerId: String?,
    val createdByCustomer: Boolean = true,
    val createdByAdmin: Boolean = false,
    val billingDetails: BillingDetails?
)

data class OrderItemGroup(
    val listItemId: Int,
    val items: List<OrderItem>
)

data class OrderItem(
    val productId: String,
    val productName: String,
    val vendorId: String,
    val quantity: Int,
    val price: Double,
    val productStatus: Int
)

data class BillingDetails(
    val customerName: String,
    val email: String,
    val phone: String?,
    val singleBillingAddress: String,
    val billingAddress: BillingAddress?
)

data class BillingAddress(
    val streetAddress: String,
    val city: String,
    val province: String,
    val postalCode: String,
    val country: String
)