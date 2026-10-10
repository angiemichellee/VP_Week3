package com.example.vp_week3.soal4


import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_week3.R
import com.example.vp_week3.ui.theme.VP_Week3Theme
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.semantics

enum class Rps(val emoji: String, val title: String) {
    ROCK("✊", "Rock"),
    PAPER("✋", "Paper"),
    SCISSOR("✌️", "Scissor")
}

enum class State {
    INITIAL, PICK, REVEAL, FINISHED
}

enum class Result(val message: String) {
    WIN("You Win!"),
    LOSE("You Lose"),
    DRAW("Draw")
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    val totalN = 5
    val targetScore = (totalN / 2) + 1

    // State permainan & skor (disimpan menggunakan rememberSaveable sesuai Notes)
    var gameState by rememberSaveable { mutableStateOf(State.INITIAL) }
    var userScore by rememberSaveable { mutableIntStateOf(0) }
    var cpuScore by rememberSaveable { mutableIntStateOf(0) }
    var bestScore by rememberSaveable { mutableIntStateOf(0) }

    // State per ronde
    var userChoice by remember { mutableStateOf<Rps?>(null) }
    var cpuChoice by remember { mutableStateOf<Rps?>(null) }
    var roundResult by remember { mutableStateOf<Result?>(null) }
    var buttonOptions by remember { mutableStateOf(Rps.values().toList()) }

    fun shuffleButtons() {
        buttonOptions = Rps.values().toList().shuffled()
    }

    fun startNewGame() {
        userScore = 0
        cpuScore = 0
        userChoice = null
        cpuChoice = null
        roundResult = null
        shuffleButtons()
        gameState = State.PICK
    }

    fun onChoiceSelected(chosen: Rps) {
        if (gameState != State.PICK) return

        val cpu = Rps.values().random()
        userChoice = chosen
        cpuChoice = cpu

        val res = when {
            chosen == cpu -> Result.DRAW
            (chosen == Rps.ROCK && cpu == Rps.SCISSOR) ||
                    (chosen == Rps.PAPER && cpu == Rps.ROCK) ||
                    (chosen == Rps.SCISSOR && cpu == Rps.PAPER) -> Result.WIN
            else -> Result.LOSE
        }

        roundResult = res
        if (res == Result.WIN) {
            userScore++
        } else if (res == Result.LOSE) {
            cpuScore++
        }

        // Hitung best score (selisih skor pemain - cpu)
        val currentDiff = userScore - cpuScore
        if (currentDiff > bestScore) {
            bestScore = currentDiff
        }

        gameState = State.REVEAL
    }

    // Delay reveal ~700 ms agar UI tetap responsif
    LaunchedEffect(gameState) {
        if (gameState == State.REVEAL) {
            delay(700L)
            if (userScore >= targetScore || cpuScore >= targetScore) {
                gameState = State.FINISHED
            } else {
                userChoice = null
                cpuChoice = null
                roundResult = null
                shuffleButtons()
                gameState = State.PICK
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Skor & Label Mode (Konsisten di semua state)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🧑 $userScore — $cpuScore 🤖",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            Text(
                text = "Best of $totalN",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        when (gameState) {
            State.INITIAL -> {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Rock • Paper • Scissors",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Button(
                        onClick = { startNewGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5)),
                        shape = RoundedCornerShape(18.dp),
                        contentPadding = PaddingValues(horizontal = 40.dp, vertical = 10.dp),
                        modifier = Modifier.semantics { contentDescription = "Start Game" }
                    ) {
                        Text("Start", color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            State.PICK -> {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Pick your move!",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    // Area VS kosong
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "?", fontSize = 28.sp, color = Color.LightGray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "VS", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "?", fontSize = 28.sp, color = Color.LightGray)
                    }
                    Spacer(modifier = Modifier.height(48.dp))

                    // 3 Tombol Besar dengan Posisi Teracak
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        buttonOptions.forEach { rpsItem ->
                            Button(
                                onClick = { onChoiceSelected(rpsItem) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCFD8DC)),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .semantics { contentDescription = "${rpsItem.emoji} ${rpsItem.title}" },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "${rpsItem.emoji} ${rpsItem.title}",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }

            State.REVEAL -> {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Baris ikon: User VS CPU
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = userChoice?.emoji ?: "", fontSize = 42.sp)
                        Spacer(modifier = Modifier.width(18.dp))
                        Text(
                            text = "VS",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(18.dp))
                        Text(text = cpuChoice?.emoji ?: "", fontSize = 42.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Hasil Singkat (Win/Lose/Draw)
                    Text(
                        text = roundResult?.message ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray,
                        modifier = Modifier.semantics {
                            contentDescription = "Hasil ronde: ${roundResult?.message}"
                        }
                    )
                }
            }

            State.FINISHED -> {
                val isWinner = userScore >= targetScore
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isWinner) "You Win the Match!" else "You Lose the Match",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Best Score: $bestScore",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = { startNewGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5)),
                        shape = RoundedCornerShape(18.dp),
                        contentPadding = PaddingValues(horizontal = 34.dp, vertical = 8.dp),
                        modifier = Modifier.semantics { contentDescription = "Restart Game" }
                    ) {
                        Text("Restart", color = Color.White, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            userScore = 0
                            cpuScore = 0
                            gameState = State.INITIAL
                        },
                        shape = RoundedCornerShape(18.dp),
                        contentPadding = PaddingValues(horizontal = 42.dp, vertical = 8.dp),
                        modifier = Modifier.semantics { contentDescription = "Exit to Main Screen" }
                    ) {
                        Text("Exit", color = Color.DarkGray, fontSize = 13.sp)
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