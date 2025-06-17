package com.budgetbee.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.budgetbee.app.data.local.db.AppDatabase
import com.budgetbee.app.ui.theme.BudgetBeeAppTheme
import com.budgetbee.app.navigation.AppNavigation
import com.budgetbee.app.utils.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetBeeAppTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                val database = AppDatabase.getInstance(context)
                val sessionManager = SessionManager
                AppNavigation(
                    navController,
                    database,
                    sessionManager
                )
            }
        }
    }
}