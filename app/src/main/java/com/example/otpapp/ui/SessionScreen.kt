package com.example.otpapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.otpapp.ui.theme.GenZBlack
import com.example.otpapp.ui.theme.GenZBlue
import com.example.otpapp.ui.theme.GenZNeon
import com.example.otpapp.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.*

private val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

@Composable
fun SessionScreen(state: UiState.Session, onLogout: () -> Unit) {
    val start = timeFmt.format(Date(state.startTimeMillis))
    val elapsed = state.elapsedMillis / 1000
    val mm = (elapsed / 60).toInt().toString().padStart(2, '0')
    val ss = (elapsed % 60).toInt().toString().padStart(2, '0')

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GenZBlack)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(GenZNeon),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.email.take(1).uppercase(),
                    style = MaterialTheme.typography.h4,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.h4,
                color = Color.White
            )
            Text(
                text = state.email,
                style = MaterialTheme.typography.body1,
                color = GenZNeon,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                backgroundColor = Color(0xFF2D2D2D),
                elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("ACTIVE SESSION", color = Color.Gray, style = MaterialTheme.typography.overline)
                    Text(
                        text = "$mm:$ss",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Started at $start", color = Color.Gray, style = MaterialTheme.typography.caption)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.DarkGray)
            ) {
                Text("LOGOUT", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
