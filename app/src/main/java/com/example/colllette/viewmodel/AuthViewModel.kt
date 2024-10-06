package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colllette.data.TokenManager
import com.example.colllette.data.local.AppDatabase
import com.example.colllette.data.local.UserEntity
import com.example.colllette.network.*
import com.example.colllette.repositories.AccountInactiveException
import com.example.colllette.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.colllette.mappers.toUserEntity

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application.applicationContext)
    private val userDao = AppDatabase.getInstance(application).userDao()

    private val _authResponse = MutableStateFlow<AuthResponse?>(null)
    val authResponse: StateFlow<AuthResponse?> = _authResponse

    private val _inactiveAccount = MutableStateFlow(false)
    val inactiveAccount: StateFlow<Boolean> = _inactiveAccount

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(username, password)
                _authResponse.value = response

                // Save token
                TokenManager.saveToken(getApplication(), response.token)

                // Map AuthResponse to UserEntity
                val userEntity = response.toUserEntity(username)

                userDao.insertUser(userEntity)
            } catch (e: AccountInactiveException) {
                _inactiveAccount.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun register(registerDto: UserRegisterDto) {
        viewModelScope.launch {
            try {
                val user = repository.register(registerDto)
                // Handle successful registration
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // Function to get the current user from the database
    fun getCurrentUser(): StateFlow<UserEntity?> {
        val userFlow = MutableStateFlow<UserEntity?>(null)
        viewModelScope.launch {
            val user = userDao.getCurrentUser()
            userFlow.value = user
        }
        return userFlow
    }
}