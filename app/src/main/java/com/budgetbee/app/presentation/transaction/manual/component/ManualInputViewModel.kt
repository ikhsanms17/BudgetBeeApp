package com.budgetbee.app.presentation.transaction.manual.component

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetbee.app.data.local.db.AppDatabase
import com.budgetbee.app.data.local.entity.TransactionEntity
import com.budgetbee.app.utils.Logger
import com.budgetbee.app.utils.convertToIsoDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransactionFormState(
    val name: String = "",
    val quantity: String = "",
    val price: String = "",
    val place: String = "",
    val date: String = ""
)

class ManualInputViewModel : ViewModel() {
    var uiState by mutableStateOf(ManualInputUiState())
        private set

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value)
    }

    fun onQuantityChange(value: String) {
        uiState = uiState.copy(quantity = value)
    }

    fun onPriceChange(value: String) {
        uiState = uiState.copy(price = value)
    }

    fun onPlaceChange(value: String) {
        uiState = uiState.copy(place = value)
    }

    fun onDateChange(value: String) {
        uiState = uiState.copy(date = value)
    }

    fun saveTransaction(context: Context) {
        val db = AppDatabase.getInstance(context)
        val dao = db.transactionDao()
        val isoDate = convertToIsoDate(uiState.date)

        val transaction = TransactionEntity(
            name = uiState.name,
            quantity = uiState.quantity.toIntOrNull() ?: 0,
            price = uiState.price.toIntOrNull() ?: 0,
            place = uiState.place,
            date = isoDate
        )

        viewModelScope.launch {
            dao.insertTransaction(transaction)
            Logger.d("TransactionInput", "Transaksi tersimpan: $transaction")
        }
    }

}
