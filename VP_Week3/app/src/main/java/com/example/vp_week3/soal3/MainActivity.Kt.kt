package com.example.vp_week3.soal3


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

enum class GameState { START, COUNTDOWN, RUNNING, GAME_OVER }
enum class GameMode { COLOR, TEXT }
enum class Choice

enum class ColorName(val label: String, val color: Color) {
    RED("RED", Color(0xFFE53935)),
    BLUE("BLUE", Color(0xFF1E88E5)),
    GREEN("GREEN", Color(0xFF43A047)),
    ORANGE("ORANGE", Color(0xFFFB8C00)),
    PURPLE("PURPLE", Color(0xFF8E24AA))
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    var gameState by remember { mutableStateOf(GameState.START) }
    var score by remember { mutableIntStateOf(0) }
    var strikes by remember { mutableIntStateOf(0) }
    var bestScore by remember { mutableIntStateOf(0) }
    var countdownText by remember { mutableStateOf("3") }
    var currentMode by remember { mutableStateOf(GameMode.COLOR) }
    var wordColor by remember { mutableStateOf(ColorName.RED) }
    var inkColor by remember { mutableStateOf(ColorName.BLUE) }
    var isLeftInk by remember { mutableStateOf(true) } // Flag boolean acak tombol
    val totalTimeMs = 5000L
    var timeLeftMs by remember { mutableLongStateOf(totalTimeMs) }
    var isTimerActive by remember { mutableStateOf(false) }
    var questionId by remember { mutableIntStateOf(0) }
    var isAnswered by remember { mutableStateOf(false) }

    fun generateNewQuestion() {
        currentMode = if (Random.nextBoolean()) GameMode.COLOR else GameMode.TEXT
        val availableColors = ColorName.values()
        wordColor = availableColors.random()

        var pickedInk = availableColors.random()
        while (pickedInk == wordColor) {
            pickedInk = availableColors.random()
        }
        inkColor = pickedInk

        isLeftInk = Random.nextBoolean()
        timeLeftMs = totalTimeMs
        isAnswered = false
        questionId++ // Memicu loop timer baru untuk soal baru
    }

    fun startCountdown() {
        score = 0
        strikes = 0
        gameState = GameState.COUNTDOWN
    }

    fun handleAnswer(isCorrect: Boolean) {
        if (isAnswered || gameState != GameState.RUNNING) return
        isAnswered = true

        if (isCorrect) {
            score++
            if (score > bestScore) bestScore = score
            generateNewQuestion()
        } else {
            strikes++
            if (strikes >= 3) {
                gameState = GameState.GAME_OVER
            } else {
                generateNewQuestion()
            }
        }
    }

    // Effect untuk alur countdown "3 -> 2 -> 1 -> Start!"
    LaunchedEffect(gameState) {
        if (gameState == GameState.COUNTDOWN) {
            val stages = listOf("3", "2", "1")
            for (num in stages) {
                countdownText = num
                delay(1000L)
            }
            countdownText = "Start!"
            delay(700L)
            generateNewQuestion()
            gameState = GameState.RUNNING
        }
    }

    LaunchedEffect(gameState, questionId) {
        if (gameState == GameState.RUNNING) {
            while (timeLeftMs > 0 && !isAnswered) {
                delay(100L)
                timeLeftMs -= 100L
            }
            if (!isAnswered && timeLeftMs <= 0) {
                handleAnswer(false) // Timeout terhitung salah
            }
        }
    }

    when (gameState) {
        GameState.START -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome\nto\nColor Word Matching",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    lineHeight = 34.sp
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = { startCountdown() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90A4AE)),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp)
                ) {
                    Text("Start Game", color = Color.White, fontSize = 16.sp)
                }
            }
        }

        GameState.COUNTDOWN -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = countdownText,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
        }

        GameState.RUNNING -> {
            val leftButtonLabel = if (isLeftInk) inkColor.label else wordColor.label
            val rightButtonLabel = if (isLeftInk) wordColor.label else inkColor.label
            val targetLabel = if (currentMode == GameMode.COLOR) inkColor.label else wordColor.label

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header: Mode & Skor/Strikes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mode: ${currentMode.name}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "✓ $score", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "✕ $strikes/3", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Clue Petunjuk Mode
                Text(
                    text = if (currentMode == GameMode.COLOR) "Pilih WARNA TINTA" else "Pilih NAMA KATA",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f))

                // Tampilan Detik Bulat
                val displaySeconds = (timeLeftMs / 1000).toInt()
                Text(
                    text = "${displaySeconds} s",
                    fontSize = 22.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Kata Utama dengan Warna Tinta
                Text(
                    text = wordColor.label,
                    color = inkColor.color,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                // Dua Tombol Jawaban (Posisi Teracak)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { handleAnswer(leftButtonLabel == targetLabel) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5)),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .height(52.dp)
                    ) {
                        Text(text = leftButtonLabel, color = Color.White, fontSize = 15.sp)
                    }

                    Button(
                        onClick = { handleAnswer(rightButtonLabel == targetLabel) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5)),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .height(52.dp)
                    ) {
                        Text(text = rightButtonLabel, color = Color.White, fontSize = 15.sp)
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }

        GameState.GAME_OVER -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Game Over!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Your Score", fontSize = 16.sp, color = Color.Gray)
                Text(
                    text = "$score",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Best Score: $bestScore", fontSize = 16.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = { startCountdown() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90A4AE)),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
                ) {
                    Text("Restart Game", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { gameState = GameState.START },
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 12.dp)
                ) {
                    Text("Exit", color = Color.DarkGray, fontSize = 15.sp)
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