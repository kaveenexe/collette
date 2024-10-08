package com.example.colllette.network

import java.time.LocalDateTime

data class OrderCancellation(
    val id: String,
    val orderId: String,
    val cancellationApproved: Boolean,
    val cancellationDate: LocalDateTime?,
    val cancelRequestStatus: CancelRequestStatus
)

enum class CancelRequestStatus {
    Pending,
    Accepted,
    Rejected
}