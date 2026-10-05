package com.example.vp_week3.Soal1

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.vp_week3.ui.theme.VP_Week3Theme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kotlin.random.Random


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

enum class tampilan{
    START,
    WAITING,
    GO,
    RESULT,
    FAILED,
    FINAL
}

enum class result(val title: String) {
    FAST("DANG YOU ARE SO FAST BRO!"),
    GOOD("YOUR REFLEX IS GOOD"),
    NORMAL("MEH LIKE OTHER PERSON"),
    SLOW("TOO SLOW BRO")
}

fun timeCategory(avgTime: Long): result{
    return when {
        avgTime < 180 -> result.FAST
        avgTime < 280 -> result.GOOD
        avgTime < 450 -> result.NORMAL
        else -> result.SLOW
    }
}
@Composable
fun Screen(modifier: Modifier = Modifier) {
    var Tampilan by rememberSaveable {mutableStateOf(tampilan.START)}

    LaunchedEffect(Tampilan) {
        if (Tampilan == tampilan.WAITING) {
            val randomDelay = Random.nextLong(2000L, 5000L)
            delay(randomDelay)
            Tampilan = tampilan.GO
        }
    }

    if (Tampilan == tampilan.START) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
                .background(Color.Cyan)
                .clickable {
                    Tampilan = tampilan.WAITING
                }
        ) {

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
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 20.dp)
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }

    else if (Tampilan == tampilan.WAITING) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
                .background(Color.Gray)
                .clickable {
                    Tampilan = tampilan.FAILED
                }
        ) {

            Text(
                text = "Get ready",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "hati-hati",
                tint = Color.White,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 20.dp)
            )

            Text(
                text= "Wait for green light",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "DON'T CLICK YET!",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }

    else if (Tampilan == tampilan.GO) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
                .background(Color.Green)
                .clickable {
                    Tampilan = tampilan.FAILED
                }
        ) {

            Text(
                text = "Go",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Icon(
                imageVector = Icons.Default.DirectionsRun,
                contentDescription = "lariiiii",
                tint = Color.White,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 20.dp)
            )

            Text(
                text= "Click Now!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "Tap as fast as you can!",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }

    else if (Tampilan == tampilan.FAILED) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
                .background(Color.Red)
                .clickable {
                    Tampilan = tampilan.FAILED
                }
        ) {

            Text(
                text = "Fail!",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Icon(
                imageVector = Icons.Default.DirectionsRun,
                contentDescription = "lariiiii",
                tint = Color.White,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 20.dp)
            )

            Text(
                text= "You clicked too early, TRY TO READ THE RULE BRO",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,

                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "TRY AGAIN",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 10.dp)
            )
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