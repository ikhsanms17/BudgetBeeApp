package com.budgetbee.app.presentation.transaction.voice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.budgetbee.app.data.local.db.AppDatabase
import com.budgetbee.app.data.local.entity.TransactionEntity
import com.budgetbee.app.presentation.transaction.voice.component.parseTransactionFromSpeech
import com.budgetbee.app.utils.ExtractData
import com.budgetbee.app.utils.Logger
import com.budgetbee.app.utils.convertWordsToNumbers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceInputScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var hasilSuara by remember { mutableStateOf("") }
    var sedangMerekam by remember { mutableStateOf(false) }
    var tampilDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val text = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            hasilSuara = text.orEmpty()
            Logger.d("VoiceInput", "Hasil suara dikenali: $hasilSuara")
        } else {
            Logger.w("VoiceInput", "Perekaman dibatalkan atau gagal")
        }
        sedangMerekam = false
    }

    LaunchedEffect(Unit) {
        ActivityCompat.requestPermissions(
            activity ?: return@LaunchedEffect,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            1
        )
    }

    fun mulaiRekam() {
        sedangMerekam = true
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Silakan ucapkan transaksi kamu...")
        }
        launcher.launch(intent)
    }

    val parsedText = remember(hasilSuara) {
        if (hasilSuara.isNotBlank()) convertWordsToNumbers(hasilSuara) else ""
    }

    val extractedData = remember(parsedText) {
        if (parsedText.isNotBlank()) ExtractData(parsedText).result else emptyMap()
    }

    val transaksiHasil = remember(parsedText) {
        parseTransactionFromSpeech(parsedText)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Suara") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = hasilSuara,
                onValueChange = { hasilSuara = it },
                label = { Text("Hasil Suara") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Contoh: saya membeli [jumlah] buah [barang] dengan harga [Harga] di [tempat]",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = { mulaiRekam() }, enabled = !sedangMerekam) {
                    Text("Mulai Rekam")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (hasilSuara.isNotBlank()) {
                            tampilDialog = true
                        } else {
                            Toast.makeText(context, "Belum ada input suara", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Konfirmasi")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { hasilSuara = "" }) {
                Text("Reset")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (parsedText.isNotBlank()) {
                        Text("Hasil Parsing: $parsedText")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (extractedData.isNotEmpty()) {
                        Text("Barang  : ${extractedData["barang"] ?: "-"}")
                        Text("Jumlah  : ${extractedData["jumlah"] ?: "-"}")
                        Text("Harga   : Rp ${extractedData["harga"] ?: "-"}")
                        Text("Tempat  : ${extractedData["tempat"] ?: "-"}")
                    } else {
                        Text("Belum ada input yang dikenali.")
                    }
                }
            }
        }

        if (tampilDialog) {
            AlertDialog(
                onDismissRequest = { tampilDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val name = extractedData["barang"] ?: "Tidak ditemukan"
                        val quantityStr = extractedData["jumlah"] ?: "Tidak ditemukan"
                        val priceStr = extractedData["harga"] ?: "Tidak ditemukan"
                        val place = extractedData["tempat"] ?: "Tidak ditemukan"

                        // Cek validasi input
                        if (name == "Tidak ditemukan" || quantityStr == "Tidak ditemukan" ||
                            priceStr == "Tidak ditemukan" || place == "Tidak ditemukan"
                        ) {
                            Toast.makeText(context, "Gagal: Data transaksi tidak lengkap atau tidak dikenali.", Toast.LENGTH_SHORT).show()
                            return@TextButton
                        }

                        val quantity = quantityStr.toIntOrNull() ?: 0
                        val price = priceStr.toIntOrNull() ?: 0
                        val date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

                        val db = AppDatabase.getInstance(context)
                        val dao = db.transactionDao()
                        val transaction = TransactionEntity(
                            name = name,
                            quantity = quantity,
                            price = price,
                            place = place,
                            date = date
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            dao.insertTransaction(transaction)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                tampilDialog = false
                                onNavigateBack()
                            }
                        }
                    }) {
                        Text("Simpan")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { tampilDialog = false }) {
                        Text("Batal")
                    }
                },
                title = { Text("Konfirmasi Transaksi") },
                text = {
                    Column {
                        Text("Nama: ${extractedData["barang"] ?: "-"}")
                        Text("Jumlah: ${extractedData["jumlah"] ?: "-"}")
                        Text("Harga: Rp${extractedData["harga"] ?: "-"}")
                        Text("Tempat: ${extractedData["tempat"] ?: "-"}")
                        Text("Tanggal: ${LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)}")
                    }
                }
            )
        }


//        if (tampilDialog) {
//            AlertDialog(
//                onDismissRequest = { tampilDialog = false },
//                confirmButton = {
//                    TextButton(onClick = {
//                        val db = AppDatabase.getInstance(context)
//                        val dao = db.transactionDao()
//                        val transaction = TransactionEntity(
//                            name = transaksiHasil.name,
//                            quantity = transaksiHasil.quantity,
//                            price = transaksiHasil.price,
//                            place = transaksiHasil.place,
//                            date = transaksiHasil.date
//                        )
//
//                        CoroutineScope(Dispatchers.IO).launch {
//                            dao.insertTransaction(transaction)
//                            withContext(Dispatchers.Main) {
//                                Toast.makeText(context, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show()
//                                tampilDialog = false
//                                onNavigateBack()
//                            }
//                        }
//                    }) {
//                        Text("Simpan")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { tampilDialog = false }) {
//                        Text("Batal")
//                    }
//                },
//                title = { Text("Konfirmasi Transaksi") },
//                text = {
//                    Column {
//                        Text("Nama: ${transaksiHasil.name}")
//                        Text("Jumlah: ${transaksiHasil.quantity}")
//                        Text("Harga: Rp${transaksiHasil.price}")
//                        Text("Tempat: ${transaksiHasil.place}")
//                        Text("Tanggal: ${transaksiHasil.date}")
//                    }
//                }
//            )
//        }
    }
}


