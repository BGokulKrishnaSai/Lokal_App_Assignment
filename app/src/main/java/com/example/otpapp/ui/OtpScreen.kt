package com.example.otpapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.otpapp.ui.theme.GenZBlack
import com.example.otpapp.ui.theme.GenZNeon
import com.example.otpapp.ui.theme.GenZPink
import com.example.otpapp.viewmodel.UiState

@Composable
fun OtpScreen(state: UiState.Otp, onVerify: (String) -> Unit, onResend: () -> Unit) {
    var otpInput by rememberSaveable { mutableStateOf("") }
    val isReady = otpInput.length == 6

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GenZBlack)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Verify Code",
                style = MaterialTheme.typography.h4,
                color = Color.White
            )
            Text(
                text = "Sent to ${state.email}",
                style = MaterialTheme.typography.body1,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            TextField(
                value = otpInput,
                onValueChange = { if (it.length <= 6) otpInput = it.filter { c -> c.isDigit() } },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = GenZNeon
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = GenZNeon,
                    unfocusedIndicatorColor = Color.DarkGray
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            state.error?.let {
                Text(
                    text = it,
                    color = GenZPink,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.body2
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { onVerify(otpInput) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isReady) GenZNeon else Color.DarkGray
                ),
                enabled = isReady
            ) {
                Text("VERIFY", fontWeight = FontWeight.Bold, color = if (isReady) Color.Black else Color.Gray)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Resend in ${state.secondsLeft}s",
                    color = Color.Gray
                )
                TextButton(onClick = onResend, enabled = state.secondsLeft == 0L) {
                    Text(
                        "RESEND",
                        color = if (state.secondsLeft == 0L) GenZNeon else Color.DarkGray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
