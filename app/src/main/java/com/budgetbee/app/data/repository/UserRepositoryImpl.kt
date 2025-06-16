package com.budgetbee.app.data.repository

import com.budgetbee.app.data.local.db.dao.UserDao
import com.budgetbee.app.data.local.entity.UserEntity
import com.budgetbee.app.domain.model.User
import com.budgetbee.app.domain.repository.UserRepository

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override suspend fun registerUser(user: User): Boolean {
        val entity = UserEntity(name = user.nama, email = user.email, password = user.password)
        userDao.insertUser(entity)
        return true
    }

    override suspend fun loginUser(identifier: String, password: String): User? {
        val entity = userDao.login(identifier, password)
        return entity?.let { User(it.id, it.name, it.email, it.password) }
    }

    override suspend fun getUser(identifier: String): User? {
        val entity = userDao.getUserByEmailOrName(identifier)
        return entity?.let { User(it.id, it.name, it.email, it.password) }
    }

//    override suspend fun updateName(id: Int, name: String) {
//        userDao.updateName(id, name)
//    }
//
//    override suspend fun updateEmail(id: Int, email: String) {
//        userDao.updateEmail(id, email)
//    }
//
//    override suspend fun updatePassword(id: Int, password: String) {
//        userDao.updatePassword(id, password)
//    }
//
//    override suspend fun deleteUser(id: Int) {
//        userDao.deleteUser(id)
//    }

}



