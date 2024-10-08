package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.colllette.data.TokenManager
import com.example.colllette.data.local.AppDatabase
import com.example.colllette.data.local.UserEntity
import com.example.colllette.mappers.toUserEntity
import com.example.colllette.network.UserApi
import com.example.colllette.network.UserUpdateDto
import com.example.colllette.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(
    application: Application,
    private val repository: UserRepository
) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getInstance(application).userDao()

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _updateStatus = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
    val updateStatus: StateFlow<UpdateStatus> = _updateStatus

    init {
        viewModelScope.launch {
            userDao.getCurrentUserFlow().collect { userEntity ->
                _user.value = userEntity
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Clear the token using DataStore
            TokenManager.clearToken(getApplication())

            // Delete user data
            userDao.deleteAllUsers()
            // The Flow will automatically emit null, updating _user.value
        }
    }
    /**
     * Initiates the update user process.
     *
     * @param firstName Updated first name.
     * @param lastName Updated last name.
     * @param username Updated username.
     * @param address Updated address.
     * @param isActive Updated active status.
     */
    fun updateUser(
        firstName: String?,
        lastName: String?,
        username: String?,
        address: String?,
        isActive: Boolean?
    ) {
        viewModelScope.launch {
            _updateStatus.value = UpdateStatus.Loading
            val currentUser = _user.value
            if (currentUser != null) {
                val updateDto = UserUpdateDto(
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    address = address,
                    isActive = isActive
                )

                val result = repository.updateUser(currentUser.id, updateDto)
                if (result.isSuccess) {
                    // Update local database with new data
                    val updatedUserEntity = updateDto.toUserEntity(currentUser)
                    userDao.insertUser(updatedUserEntity)
                    _updateStatus.value = UpdateStatus.Success
                } else {
                    _updateStatus.value = UpdateStatus.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } else {
                _updateStatus.value = UpdateStatus.Error("User not logged in")
            }
        }
    }

    /**
     * Resets the update status to idle.
     */
    fun resetUpdateStatus() {
        _updateStatus.value = UpdateStatus.Idle
    }

    /**
     * Represents the current status of the update operation.
     */
    sealed class UpdateStatus {
        object Idle : UpdateStatus()
        object Loading : UpdateStatus()
        object Success : UpdateStatus()
        data class Error(val message: String) : UpdateStatus()
    }
}
