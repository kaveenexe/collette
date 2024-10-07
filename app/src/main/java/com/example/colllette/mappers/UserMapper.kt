package com.example.colllette.mappers

import com.example.colllette.data.local.UserEntity
import com.example.colllette.network.AuthResponse

fun AuthResponse.toUserEntity(username: String): UserEntity {
    return UserEntity(
        id = this.userId,
        nic = this.nic,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        username = username,
        userType = this.role,
        isActive = true,
        contactNumber = "", // Update if available
        address = this.address
    )
}