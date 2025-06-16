package com.budgetbee.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.budgetbee.app.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :input OR name = :input LIMIT 1")
    suspend fun getUserByEmailOrName(input: String): UserEntity?

    @Query("SELECT * FROM users WHERE (email = :input OR name = :input) AND password = :password LIMIT 1")
    suspend fun login(input: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE (email = :input OR name = :input) AND password = :password LIMIT 1")
    suspend fun getUserByIdentifier(input: String, password: String): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET name = :name WHERE id = :id")
    suspend fun updateName(id: Int, name: String)

    @Query("UPDATE users SET email = :email WHERE id = :id")
    suspend fun updateEmail(id: Int, email: String)

    @Query("UPDATE users SET password = :password WHERE id = :id")
    suspend fun updatePassword(id: Int, password: String)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Int)

}