package com.budgetbee.app.presentation.report

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetbee.app.data.local.db.AppDatabase
import com.budgetbee.app.domain.model.NameSummary
import com.budgetbee.app.domain.model.PlaceSummary
import kotlinx.coroutines.launch

class ReportViewModel(context: Context) : ViewModel() {
    private val dao = AppDatabase.getInstance(context).transactionDao()

    var total by mutableIntStateOf(0)
        private set
    var nameSummary by mutableStateOf(emptyList<NameSummary>())
        private set
    var placeSummary by mutableStateOf(emptyList<PlaceSummary>())
        private set

    fun loadAll() {
        viewModelScope.launch {
            total = dao.getTotal() ?: 0
            nameSummary = dao.getSummaryByName()
            placeSummary = dao.getSummaryByPlace()
        }
    }

    fun loadDaily(date: String) {
        viewModelScope.launch {
            total = dao.getTotalByDate(date) ?: 0
            nameSummary = dao.getSummaryByNameByDate(date)
            placeSummary = dao.getSummaryByPlaceByDate(date)
        }
    }

    fun loadWeekly(start: String, end: String) {
        viewModelScope.launch {
            total = dao.getTotalBetween(start, end) ?: 0
            nameSummary = dao.getSummaryByNameBetween(start, end)
            placeSummary = dao.getSummaryByPlaceBetween(start, end)
        }
    }

    fun loadMonthly(month: Int) {
        val monthStr = month.toString().padStart(2, '0')
        viewModelScope.launch {
            total = dao.getTotalByMonth(monthStr) ?: 0
            nameSummary = dao.getSummaryByNameByMonth(monthStr)
            placeSummary = dao.getSummaryByPlaceByMonth(monthStr)
        }
    }
}



