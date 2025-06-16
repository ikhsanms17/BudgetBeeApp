package com.budgetbee.app.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.budgetbee.app.data.local.db.AppDatabase
import com.budgetbee.app.presentation.transaction.TransactionViewModel

class TransactionViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
