package com.example.rakhimovakp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.rakhimovakp.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    suspend fun registerUser(user: User)

    @Insert(onConflict = REPLACE)
    suspend fun registerUsers(users: List<User>)

    @Query("select * from User")
    suspend fun getUsers(): List<User>
}