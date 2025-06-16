package com.budgetbee.app.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.budgetbee.app.data.local.db.dao.TransactionDao
import com.budgetbee.app.data.local.db.dao.UserDao
import com.budgetbee.app.data.local.entity.TransactionEntity
import com.budgetbee.app.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budgetbee_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}