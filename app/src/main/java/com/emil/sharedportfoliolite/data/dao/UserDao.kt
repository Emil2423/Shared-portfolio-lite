package com.emil.sharedportfoliolite.data.dao

import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.emil.sharedportfoliolite.data.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Delete
    suspend fun delete(user: User)
}