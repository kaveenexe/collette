package com.example.colllette.repositories

import com.example.colllette.network.UserApi
import com.example.colllette.network.UserUpdateDto
import com.example.colllette.data.local.UserDao
import com.example.colllette.data.local.UserEntity
import com.example.colllette.mappers.toUserEntity
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val userAPI: UserApi,
    private val userDao: UserDao
) {
    // ... existing repository functions

    /**
     * Updates the user details by making a PUT request to the backend.
     * On success, updates the local database with the new details.
     *
     * @param userId The ID of the user to update.
     * @param updateDto The details to update.
     * @return Result indicating success or failure.
     */
    suspend fun updateUser(userId: String, updateDto: UserUpdateDto): Result<Unit> {
        return try {
            val response = userAPI.updateUser(userId, updateDto)
            if (response.isSuccessful) {
                // Assuming the backend doesn't return the updated user, fetch it from the local database
                // Alternatively, if the backend returns the updated user, map and insert it
                val updatedUser = userDao.getCurrentUserFlow().first()
                if (updatedUser != null) {
                    val newUserEntity = updateDto.toUserEntity(updatedUser)
                    userDao.insertUser(newUserEntity)
                }
                Result.success(Unit)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: IOException) {
            Result.failure(e) // Network or conversion error
        } catch (e: HttpException) {
            Result.failure(e) // HTTP error
        }
    }
}
