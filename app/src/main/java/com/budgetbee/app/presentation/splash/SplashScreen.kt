package com.budgetbee.app.presentation.splash

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.budgetbee.app.utils.SessionManager
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController, context: Context = LocalContext.current) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val isLoggedIn = SessionManager.isLoggedIn(context)
                val session = SessionManager.getSession(context)

                if (isLoggedIn && session != null) {
                    navController.navigate("dashboard") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                // Kalau ada error, fallback ke login
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("BudgetBee 🐝", style = MaterialTheme.typography.headlineMedium)
    }
}

