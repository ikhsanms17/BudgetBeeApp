package com.budgetbee.app.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.budgetbee.app.data.local.db.AppDatabase
import com.budgetbee.app.utils.SessionManager
import com.budgetbee.app.utils.Logger
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    val identifier = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val error = remember { mutableStateOf<String?>(null) }

    val db = remember { AppDatabase.getInstance(context) }
    val userDao = db.userDao()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val session = SessionManager.getSession(context)
        if (session != null) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = identifier.value,
            onValueChange = { identifier.value = it },
            label = { Text("Email atau Nama") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(8.dp))
        error.value?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    val user = userDao.getUserByIdentifier(identifier.value, password.value)
                    Logger.d("Login", "Percobaan login dengan input: ${identifier.value}")
                    if (user != null) {
                        SessionManager.saveSession(context, user.id, user.name)
                        SessionManager.setLoggedIn(context, true)
                        Logger.d("Login", "Login berhasil: userId=${user.id}, nama=${user.name}")
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        Logger.w("Login", "Login gagal untuk input: ${identifier.value}")
                        error.value = "Email/nama atau password salah"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Masuk")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Text("Belum punya akun?")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Daftar",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}
