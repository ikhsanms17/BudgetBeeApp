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
import com.budgetbee.app.utils.SessionManager
import kotlinx.coroutines.launch

class ReportViewModel(context: Context) : ViewModel() {
    private val dao = AppDatabase.getInstance(context).transactionDao()

    var total by mutableIntStateOf(0)
        private set
    var nameSummary by mutableStateOf(emptyList<NameSummary>())
        private set
    var placeSummary by mutableStateOf(emptyList<PlaceSummary>())
        private set

    private var userId: Int = -1

    init {
        viewModelScope.launch {
            userId = SessionManager.getUserId(context) ?: -1
            loadAll() // opsional: auto-load saat userId sudah siap
        }
    }

    fun loadAll() {
        viewModelScope.launch {
            total = dao.getTotal(userId) ?: 0
            nameSummary = dao.getSummaryByName(userId)
            placeSummary = dao.getSummaryByPlace(userId)
        }
    }

    fun loadDaily(date: String) {
        viewModelScope.launch {
            total = dao.getTotalByDate(date, userId) ?: 0
            nameSummary = dao.getSummaryByNameByDate(date, userId)
            placeSummary = dao.getSummaryByPlaceByDate(date, userId)
        }
    }

    fun loadWeekly(start: String, end: String) {
        viewModelScope.launch {
            total = dao.getTotalBetween(start, end, userId) ?: 0
            nameSummary = dao.getSummaryByNameBetween(start, end, userId)
            placeSummary = dao.getSummaryByPlaceBetween(start, end, userId)
        }
    }

    fun loadMonthly(month: Int) {
        val monthStr = month.toString().padStart(2, '0')
        viewModelScope.launch {
            total = dao.getTotalByMonth(monthStr, userId) ?: 0
            nameSummary = dao.getSummaryByNameByMonth(monthStr, userId)
            placeSummary = dao.getSummaryByPlaceByMonth(monthStr, userId)
        }
    }
}



