package com.budgetbee.app.presentation.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.budgetbee.app.domain.repository.UserRepository
import com.budgetbee.app.navigation.Screen
import com.budgetbee.app.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AccountScreen(
    navController: NavHostController,
    userRepository: UserRepository
) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }

    var showEditNameDialog by remember { mutableStateOf(false) }
    var showEditEmailDialog by remember { mutableStateOf(false) }
    var showEditPasswordDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userName = SessionManager.getUserName(context) ?: "Pengguna"
        userEmail = SessionManager.getUserEmail(context) ?: "-"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Nama", style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                    Text(userName, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
                }
                Text("edit", color = Color.White, modifier = Modifier.clickable {
                    // TODO: Aksi edit nama
                })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Email", style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                    Text(userEmail, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
                }
                Text("edit", color = Color.White, modifier = Modifier.clickable {
                    // TODO: Aksi edit email
                })
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("ganti password anda", color = Color.White, modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // TODO: Ganti password
                }
                .padding(vertical = 8.dp)
            )

            Text("history", color = Color.White, modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Screen.History.route)
                }
                .padding(vertical = 8.dp)
            )

            Text("laporan", color = Color.White, modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Screen.Report.route)
                }
                .padding(vertical = 8.dp)
            )

            Text("hapus akun", color = Color.White, modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // TODO: Konfirmasi hapus akun
                }
                .padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    SessionManager.clearSession(context)
                    withContext(Dispatchers.Main) {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC107), // Kuning
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text("LOGOUT")
        }
    }
}




