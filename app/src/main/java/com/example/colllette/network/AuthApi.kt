package com.example.colllette.network

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/Auth/login")
    suspend fun login(@Body loginDto: UserLoginDto): AuthResponse

    @POST("api/Auth/register")
    suspend fun register(@Body registerDto: UserRegisterDto): User
}