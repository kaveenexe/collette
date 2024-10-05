package com.example.colllette.network

data class User(
    val id: String,
    val nic: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val userType: String,
    val isActive: Boolean,
    val contactNumber: String,
    val address: String
)