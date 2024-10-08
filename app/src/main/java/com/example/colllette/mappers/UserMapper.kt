package com.example.colllette.mappers

import com.example.colllette.data.local.UserEntity
import com.example.colllette.network.AuthResponse
import com.example.colllette.network.UserUpdateDto

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

fun UserUpdateDto.toUserEntity(existingUser: UserEntity): UserEntity {
    return existingUser.copy(
        firstName = this.firstName ?: existingUser.firstName,
        lastName = this.lastName ?: existingUser.lastName,
        username = this.username ?: existingUser.username,
        address = this.address ?: existingUser.address,
        isActive = this.isActive ?: existingUser.isActive
    )
}