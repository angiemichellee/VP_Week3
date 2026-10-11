package com.example.vp_week3.BonusSoal2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_week3.ui.theme.VP_Week3Theme
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_week3.ui.theme.VP_Week3Theme
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VP_Week3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Screen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun Screen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // State Input
    var favoriteSpot by rememberSaveable { mutableStateOf("Fjellheisen Cable Car") }
    var experience by rememberSaveable {
        mutableStateOf("Pemandangan langit malam hijau aurora spektakuler dari puncak gunung...")
    }
    var additionalNotes by rememberSaveable { mutableStateOf("") }
    var selectedSatisfaction by rememberSaveable { mutableStateOf("Luar Biasa ★") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF0F172A),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val pesanToast = if (favoriteSpot.isNotBlank()) {
                        "Jurnal disimpan: $favoriteSpot"
                    } else {
                        "Jurnal perjalanan berhasil disimpan!"
                    }
                    Toast.makeText(context, pesanToast, Toast.LENGTH_SHORT).show()
                },
                containerColor = Color(0xFF38BDF8),
                contentColor = Color(0xFF0F172A),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Simpan",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = "Draft Tersimpan",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF38BDF8)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Banner Gradien Atas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF1E40AF), Color(0xFF0284C7))
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Text(
                        text = "LOG PERJALANAN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF93C5FD),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tromsø, Norway",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "❄️", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ekspedisi Aurora Borealis",
                        fontSize = 13.sp,
                        color = Color(0xFFE2E8F0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Field 1: Spot Wisata Favorit (Single-line)
            TextField(
                value = favoriteSpot,
                onValueChange = { favoriteSpot = it },
                label = {
                    Text(
                        "SPOT WISATA FAVORIT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Field 2: Pengalaman yang Dinikmati (Multi-line, min 3 baris)
            TextField(
                value = experience,
                onValueChange = { experience = it },
                label = {
                    Text(
                        "APA YANG PALING KAMU NIKMATI?",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Field 3: Catatan Tambahan / Perlengkapan
            TextField(
                value = additionalNotes,
                onValueChange = { additionalNotes = it },
                label = {
                    Text(
                        "CATATAN TAMBAHAN / PERLENGKAPAN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                },
                placeholder = {
                    Text(
                        "(Ketik perlengkapan ekstra di sini...)",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Pilihan Tingkat Kepuasan
            Text(
                text = "TINGKAT KEPUASAN:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8),
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Biasa", "Seru", "Luar Biasa ★").forEach { opsi ->
                    val isSelected = selectedSatisfaction == opsi
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) Color(0xFF38BDF8) else Color(0xFF1E293B),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedSatisfaction = opsi }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = opsi,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF0F172A) else Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    VP_Week3Theme {
        Screen()
    }
}