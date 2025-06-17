package com.budgetbee.app.domain.repository

import com.budgetbee.app.data.local.entity.UserEntity
import com.budgetbee.app.domain.model.User

interface UserRepository {
    suspend fun registerUser(user: User): Boolean
    suspend fun loginUser(identifier: String, password: String): User?
    suspend fun getUser(identifier: String): User?
    suspend fun updateName(userId: Int, newName: String)
    suspend fun updateEmail(userId: Int, newEmail: String)
    suspend fun updatePassword(userId: Int, newPassword: String)
    suspend fun getUserByEmail(email: String): UserEntity?
    suspend fun updatePasswordByEmail(email: String, newPassword: String)
}