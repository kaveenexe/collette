package com.example.colllette.network

data class UserUpdateDto(
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val address: String?,
    val isActive: Boolean?
)
