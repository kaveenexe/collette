package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colllette.data.TokenManager
import com.example.colllette.data.local.DatabaseProvider
import com.example.colllette.data.local.UserEntity
import com.example.colllette.network.*
import com.example.colllette.repositories.AccountInactiveException
import com.example.colllette.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application.applicationContext)
    private val userDao = DatabaseProvider.getDatabase(application).userDao()

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

                // Save user data locally
                val userEntity = response.toUserEntity()
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
}

private fun AuthResponse.toUserEntity(): UserEntity {
    return UserEntity(
        // You can add username to AuthResponse if needed
        // Add contactNumber to AuthResponse if available
        id = userId,
        nic = nic,
        email = email,
        firstName = firstName,
        lastName = lastName,
        username = "",
        userType = role,
        isActive = true, // Since the user logged in successfully
        contactNumber = "",
        address = address
    )
}