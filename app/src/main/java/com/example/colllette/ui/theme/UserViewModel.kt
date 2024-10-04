package com.example.colllette.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import com.example.colllette.data.User
import com.example.colllette.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_database"
    ).build()

    private val userDao = db.userDao()

    val users: LiveData<List<User>> = userDao.getAllUsers().asLiveData()

    fun addUser(firstName: String, lastName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(firstName = firstName, lastName = lastName)
            userDao.insert(user)
        }
    }
}
