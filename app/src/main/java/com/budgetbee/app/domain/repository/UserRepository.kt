package com.budgetbee.app.domain.repository

import com.budgetbee.app.domain.model.User

interface UserRepository {
    suspend fun registerUser(user: User): Boolean
    suspend fun loginUser(identifier: String, password: String): User?
    suspend fun getUser(identifier: String): User?
}