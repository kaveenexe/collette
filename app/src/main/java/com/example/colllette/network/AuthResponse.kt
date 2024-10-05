package com.example.colllette.network

data class AuthResponse(
    val token: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val email: String,
    val address: String,
    val nic: String
)