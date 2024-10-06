package com.example.colllette.repositories

import android.content.Context
import android.net.http.HttpException
import com.example.colllette.network.*

class AuthRepository(context: Context) {
    private val authApi = ApiClient(context).authApi

    suspend fun login(username: String, password: String): AuthResponse {
        val loginDto = UserLoginDto(username, password)
        return try {
            authApi.login(loginDto)
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 401) {
                throw AccountInactiveException("Your account is not active. Please wait for activation.")
            } else {
                throw e
            }
        }
    }

    suspend fun register(registerDto: UserRegisterDto): User {
        return authApi.register(registerDto)
    }
}

class AccountInactiveException(message: String) : Exception(message)