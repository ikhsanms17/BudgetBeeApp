package com.budgetbee.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.budgetbee.app.data.local.entity.TransactionEntity
import com.budgetbee.app.domain.model.NameSummary
import com.budgetbee.app.domain.model.PlaceSummary

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("""
        SELECT * FROM transactions
        WHERE strftime('%m', date) = strftime('%m', 'now')
        AND strftime('%Y', date) = strftime('%Y', 'now')
        ORDER BY date DESC
    """)
    suspend fun getTransactionsThisMonth(): List<TransactionEntity>

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 5")
    suspend fun getLast5(): List<TransactionEntity>

    @Query("SELECT * FROM transactions")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE date = :date")
    suspend fun getByDate(date: String): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE date >= :date")
    suspend fun getAfterDate(date: String): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE strftime('%m', date) = :month")
    suspend fun getByMonthInternal(month: String): List<TransactionEntity>

    suspend fun getByMonth(month: String): List<TransactionEntity> {
        val monthStr = month.padStart(2, '0') // "6" jadi "06"
        return getByMonthInternal(monthStr)
    }

    // Untuk Report Screen
    // Total pengeluaran
    @Query("SELECT SUM(price) FROM transactions")
    suspend fun getTotal(): Int?

    @Query("SELECT SUM(price) FROM transactions WHERE date = :date")
    suspend fun getTotalByDate(date: String): Int?

    @Query("SELECT SUM(price) FROM transactions WHERE date BETWEEN :start AND :end")
    suspend fun getTotalBetween(start: String, end: String): Int?

    @Query("SELECT SUM(price) FROM transactions WHERE strftime('%m', date) = :month")
    suspend fun getTotalByMonth(month: String): Int?

    // Group by nama barang
    @Query("SELECT name, SUM(price) as total FROM transactions GROUP BY name")
    suspend fun getSummaryByName(): List<NameSummary>

    @Query("SELECT name, SUM(price) as total FROM transactions WHERE date = :date GROUP BY name")
    suspend fun getSummaryByNameByDate(date: String): List<NameSummary>

    @Query("SELECT name, SUM(price) as total FROM transactions WHERE date BETWEEN :start AND :end GROUP BY name")
    suspend fun getSummaryByNameBetween(start: String, end: String): List<NameSummary>

    @Query("SELECT name, SUM(price) as total FROM transactions WHERE strftime('%m', date) = :month GROUP BY name")
    suspend fun getSummaryByNameByMonth(month: String): List<NameSummary>

    // Group by tempat
    @Query("SELECT place, SUM(price) as total FROM transactions GROUP BY place")
    suspend fun getSummaryByPlace(): List<PlaceSummary>

    @Query("SELECT place, SUM(price) as total FROM transactions WHERE date = :date GROUP BY place")
    suspend fun getSummaryByPlaceByDate(date: String): List<PlaceSummary>

    @Query("SELECT place, SUM(price) as total FROM transactions WHERE date BETWEEN :start AND :end GROUP BY place")
    suspend fun getSummaryByPlaceBetween(start: String, end: String): List<PlaceSummary>

    @Query("SELECT place, SUM(price) as total FROM transactions WHERE strftime('%m', date) = :month GROUP BY place")
    suspend fun getSummaryByPlaceByMonth(month: String): List<PlaceSummary>




}
