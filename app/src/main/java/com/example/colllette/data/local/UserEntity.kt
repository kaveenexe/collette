package com.example.colllette.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String, // Use 'id' from backend
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