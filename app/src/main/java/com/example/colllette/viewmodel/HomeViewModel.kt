package com.example.colllette.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.colllette.data.local.DatabaseProvider
import com.example.colllette.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = DatabaseProvider.getDatabase(application).userDao()

    val user: Flow<UserEntity?> = userDao.getUserById(getUserId())

    private fun getUserId(): String {
        // Retrieve the user ID from the stored token or preferences
        // For simplicity, let's assume we have it stored somewhere
        // Alternatively, you can store the user ID in a DataStore
        return "user_id" // Replace with actual user ID retrieval
    }
}