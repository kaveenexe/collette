package com.example.colllette.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User): Long // Non-suspend function

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
}
