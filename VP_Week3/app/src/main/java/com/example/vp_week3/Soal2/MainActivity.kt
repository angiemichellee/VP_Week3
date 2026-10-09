package com.example.vp_week3.Soal2

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


@Composable
fun Screen(modifier: Modifier = Modifier) {
    var coins by rememberSaveable() { mutableStateOf(0) }
    var coinsPerTap by rememberSaveable() { mutableStateOf(1) }
    var isPressed by remember() {mutableStateOf(false)}
    var upgradeCost by rememberSaveable() { mutableStateOf(10) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.__15),
            contentDescription = "background",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize())

        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        Column(modifier = Modifier.fillMaxSize().padding(50.dp),
                verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally
            ) {

            Card(Modifier.weight(1f).padding(50.dp),
            shape = RoundedCornerShape(30.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.5f))
            ){
                Column(modifier = Modifier.fillMaxSize().padding(30.dp),
                    verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your Coins", fontSize = 16.sp,
                        color = Color.White)

                    Text("$coins", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4CAF50))

                    Text("$coinsPerTap Coin Per Tap", fontSize = 16.sp,
                        color = Color.White)
                }
            }

            Text(
                text = "Tap the Cat!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(shape = RoundedCornerShape(30.dp),
                modifier = Modifier.size(160.dp).pointerInput(Unit) {
                    detectTapGestures (onPress = {
                        isPressed = true
                        coins += coinsPerTap
                        tryAwaitRelease()
                        isPressed = false
                    })
                }
            ){


                val gambarKucing = if (isPressed) {
                    R.drawable._7926b940e8de3b11b3058d43872e4a81_3
                } else {
                    R.drawable._7926b940e8de3b11b3058d43872e4a81_2
                }
                Image(painter = painterResource(gambarKucing),
                    contentDescription = "", contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isPressed) "Meow!" else "Purr~",
                fontSize = 16.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(shape = RoundedCornerShape(30.dp),
                modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp)) {

                    Text(
                        text = "Give me your coin",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    val nilaiBaru = (coinsPerTap * 1.5).toInt().coerceAtLeast(coinsPerTap + 1)
                    val penambahan = nilaiBaru - coinsPerTap

                    Text(
                        text = "Next upgrade: +$penambahan coins per tap",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val isCukup = coins >= upgradeCost

                    Button(onClick = {
                        if (isCukup) {
                            coins -= upgradeCost
                            coinsPerTap = nilaiBaru
                            upgradeCost *= 2
                        }
                    },
                        enabled = isCukup,
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            disabledContentColor = Color(0xFFB0B0B0)
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                        ) {

                        Text(
                            text = if (isCukup) "Pay for $upgradeCost coins" else "Find ${upgradeCost - coins} more coins",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isCukup) Color.White else Color.DarkGray
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