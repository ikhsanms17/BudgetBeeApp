package com.budgetbee.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.budgetbee.app.ui.theme.BudgetBeeAppTheme
import com.budgetbee.app.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetBeeAppTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}