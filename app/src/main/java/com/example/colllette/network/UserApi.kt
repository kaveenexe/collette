package com.example.colllette.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @PUT("api/Users/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body updateDto: UserUpdateDto
    ): Response<Unit>
}