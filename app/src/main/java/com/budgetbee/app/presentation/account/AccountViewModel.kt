package com.budgetbee.app.presentation.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetbee.app.domain.model.User
import com.budgetbee.app.domain.repository.UserRepository
import com.budgetbee.app.utils.SessionManager
import kotlinx.coroutines.launch

//class AccountViewModel(
//    private val userRepository: UserRepository,
//    private val sessionManager: SessionManager
//) : ViewModel() {
//
//    var user by mutableStateOf<User?>(null)
//        private set
//
//    init {
//        loadUser()
//    }
//
//    private fun loadUser() {
//        viewModelScope.launch {
//            val currentUserId = sessionManager.getLoggedInUserId()
//            user = currentUserId?.let { userRepository.getUserById(it) }
//        }
//    }
//
//    fun logout(onLoggedOut: () -> Unit) {
//        viewModelScope.launch {
//            sessionManager.clearSession()
//            onLoggedOut()
//        }
//    }
//}
