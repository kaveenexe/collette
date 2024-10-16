package com.example.colllette.network

import java.time.LocalDateTime

data class OrderCancellation(
    val id: String,
    val orderId: String,
    val cancellationApproved: Boolean,
    val cancellationDate: LocalDateTime?,
    val cancelRequestStatus: Int
)

enum class CancelRequestStatus(val value: Int) {
    Pending(0),
    Accepted(1),
    Rejected(2)
}