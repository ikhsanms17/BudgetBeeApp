package com.budgetbee.app.presentation.transaction

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetbee.app.data.local.db.AppDatabase
import com.budgetbee.app.data.local.entity.TransactionEntity
import com.budgetbee.app.presentation.transaction.manual.component.TransactionFormState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val db: AppDatabase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionFormState())
    val state: StateFlow<TransactionFormState> = _state

    fun onNameChange(v: String)     = update { it.copy(name = v) }
    fun onQuantityChange(v: String) = update { it.copy(quantity = v) }
    fun onPriceChange(v: String)    = update { it.copy(price = v) }
    fun onPlaceChange(v: String)    = update { it.copy(place = v) }
    fun onDateChange(v: String)     = update { it.copy(date = v) }

    fun validate(): Boolean {
        val s = _state.value
        return s.name.isNotBlank() &&
                s.quantity.toIntOrNull() != null &&
                s.price.toIntOrNull() != null &&
                s.place.isNotBlank() &&
                s.date.isNotBlank()
    }

    fun insertTransaction(context: Context) {
        val s = _state.value
        val txn = TransactionEntity(
            name = s.name,
            quantity = s.quantity.toInt(),
            price = s.price.toInt(),
            place = s.place,
            date = s.date
        )

        viewModelScope.launch(Dispatchers.IO) {
            db.transactionDao().insertTransaction(txn)
        }
    }

    private fun update(change: (TransactionFormState) -> TransactionFormState) {
        _state.update { change(it) }
    }
}
