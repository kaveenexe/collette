package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.colllette.data.local.AppDatabase
import com.example.colllette.network.ApiClient
import com.example.colllette.repositories.UserRepository

class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            // Initialize ApiClient with application context
            val apiClient = ApiClient(application)
            // Create UserRepository instance
            val userRepository = UserRepository(apiClient.userApi, AppDatabase.getInstance(application).userDao())
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(application, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
