package com.example.vp_week3.Soal1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

enum class tampilan {
    START,
    WAITING,
    GO,
    RESULT,
    FAILED,
    FINAL
}

enum class result(val title: String, val color: Color) {
    FAST("DANG YOU ARE SO FAST BRO!", Color(0xFF00C853)),      // Hijau terang
    GOOD("YOUR REFLEX IS GOOD", Color(0xFF2979FF)),             // Biru
    NORMAL("MEH LIKE OTHER PERSON", Color(0xFFFF9100)),         // Oranye
    SLOW("YOU LIKE A SNAIL BRO", Color(0xFFFF3D00))             // Oranye kemerahan
}

fun timeCategory(avgTime: Long): result {
    return when {
        avgTime < 180 -> result.FAST
        avgTime < 280 -> result.GOOD
        avgTime < 450 -> result.NORMAL
        else -> result.SLOW
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    var Tampilan by rememberSaveable { mutableStateOf(tampilan.START) }
    var currentTrial by rememberSaveable { mutableIntStateOf(1) }
    val trialResults = remember { mutableStateListOf<Long>() }

    var startMark by remember { mutableStateOf<TimeMark?>(null) }
    var reactionTimeMs by rememberSaveable { mutableLongStateOf(0L) }

    LaunchedEffect(Tampilan) {
        if (Tampilan == tampilan.WAITING) {
            val randomDelay = Random.nextLong(2000L, 5000L)
            delay(randomDelay)
            startMark = TimeSource.Monotonic.markNow()
            Tampilan = tampilan.GO
        }
    }

    when (Tampilan) {
        tampilan.START -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFF4DD0E1))
                    .clickable { Tampilan = tampilan.WAITING }
                    .padding(vertical = 48.dp, horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Reaction",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "petir",
                        tint = Color.White,
                        modifier = Modifier.size(180.dp).padding(bottom = 20.dp)
                    )
                    Text(
                        text = "Test",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Text(
                        text = "Click to Start!",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                if (trialResults.isNotEmpty()) {
                    TrialResultCard(trialResults = trialResults)
                } else {
                    Spacer(modifier = Modifier.height(1.dp))
                }
            }
        }

        tampilan.WAITING -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFBDBDBD))
                    .clickable { Tampilan = tampilan.FAILED }
                    .padding(24.dp)
            ) {
                Text(
                    text = "Get Ready",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "hati-hati",
                    tint = Color.White,
                    modifier = Modifier.size(180.dp).padding(bottom = 20.dp)
                )
                Text(
                    text = "Wait for green light...",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = "DON'T CLICK YET!",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        tampilan.GO -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFF43A047))
                    .clickable {
                        reactionTimeMs = startMark?.elapsedNow()?.inWholeMilliseconds ?: 0L
                        trialResults.add(reactionTimeMs)
                        if (currentTrial < 3) {
                            Tampilan = tampilan.RESULT
                        } else {
                            Tampilan = tampilan.FINAL
                        }
                    }
                    .padding(24.dp)
            ) {
                Text(
                    text = "GO!",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Icon(
                    imageVector = Icons.Default.DirectionsRun,
                    contentDescription = "lari",
                    tint = Color.White,
                    modifier = Modifier.size(180.dp).padding(bottom = 20.dp)
                )
                Text(
                    text = "CLICK NOW!",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = "TAP AS FAST AS YOU CAN!",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        tampilan.RESULT -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFF43A047))
                    .clickable {
                        currentTrial++
                        Tampilan = tampilan.WAITING
                    }
                    .padding(vertical = 48.dp, horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Trial $currentTrial Complete!",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Centang",
                        tint = Color.White,
                        modifier = Modifier.size(150.dp).padding(bottom = 24.dp)
                    )
                    Text(
                        text = "Time: ${reactionTimeMs}ms",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Continue to Trial ${currentTrial + 1}",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                TrialResultCard(trialResults = trialResults)
            }
        }

        tampilan.FAILED -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFE53935))
                    .clickable { Tampilan = tampilan.WAITING }
                    .padding(vertical = 48.dp, horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "FAIL!",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ThumbDown,
                        contentDescription = "Gagal",
                        tint = Color.White,
                        modifier = Modifier.size(150.dp).padding(bottom = 20.dp)
                    )
                    Text(
                        text = "You clicked too early, TRY TO\nREAD THE RULE BRO",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = "TRY AGAIN",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                if (trialResults.isNotEmpty()) {
                    TrialResultCard(trialResults = trialResults)
                } else {
                    Spacer(modifier = Modifier.height(1.dp))
                }
            }
        }

        tampilan.FINAL -> {
            val avg = if (trialResults.isNotEmpty()) trialResults.average().toLong() else 0L
            val finalCategory = timeCategory(avg)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxSize()
                    .background(finalCategory.color)
                    .clickable {
                        // Reset tes baru
                        trialResults.clear()
                        currentTrial = 1
                        Tampilan = tampilan.START
                    }
                    .padding(vertical = 48.dp, horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = finalCategory.title,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Hasil Akhir",
                        tint = Color.White,
                        modifier = Modifier.size(150.dp).padding(bottom = 20.dp)
                    )
                    Text(
                        text = "Average: ${avg}ms",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Click to Start New Test",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                TrialResultCard(trialResults = trialResults, showAverage = true, avgScore = avg)
            }
        }
    }
}

@Composable
fun TrialResultCard(
    trialResults: List<Long>,
    showAverage: Boolean = false,
    avgScore: Long = 0L
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(0.65f)
            .padding(top = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Trial Results",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "1", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                Text(text = "2", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                Text(text = "3", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = if (trialResults.size >= 1) "${trialResults[0]}ms" else "-",
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = if (trialResults.size >= 2) "${trialResults[1]}ms" else "-",
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = if (trialResults.size >= 3) "${trialResults[2]}ms" else "-",
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
            }

            if (showAverage) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Average Score",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${avgScore}ms",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
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