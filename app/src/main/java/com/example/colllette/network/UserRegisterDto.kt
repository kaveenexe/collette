package com.example.colllette.network

data class UserRegisterDto(
    val nic: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val userType: String = "Customer",
    val address: String,
    val contactNumber: String
)