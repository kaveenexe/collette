package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.colllette.data.TokenManager
import com.example.colllette.data.local.AppDatabase
import com.example.colllette.data.local.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getInstance(application).userDao()

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

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
}
