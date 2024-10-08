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

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess

    fun login(username: String, password: String) {
        viewModelScope.launch {
            // Reset states before starting the login attempt
            _inactiveAccount.value = false
            _error.value = null
            _authResponse.value = null

            try {
                val response = repository.login(username, password)

                // Save token
                TokenManager.saveToken(getApplication(), response.token)

                // Map AuthResponse to UserEntity
                val userEntity = response.toUserEntity(username)

                // Insert user into database
                userDao.insertUser(userEntity)

                // Now set the authResponse after inserting user data
                _authResponse.value = response

                // Logging for debugging
                println("AuthViewModel: User data inserted and authResponse updated.")
            } catch (e: AccountInactiveException) {
                _inactiveAccount.value = true
                println("AuthViewModel: Account is inactive.")
            } catch (e: Exception) {
                _error.value = e.message
                println("AuthViewModel: Error during login - ${e.message}")
            }
        }
    }


    fun register(registerDto: UserRegisterDto) {
        viewModelScope.launch {
            try {
                val user = repository.register(registerDto)
                _registrationSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }

    fun resetAuthResponse() {
        _authResponse.value = null
    }

    fun resetInactiveAccount() {
        _inactiveAccount.value = false
    }
}