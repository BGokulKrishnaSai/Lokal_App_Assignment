package com.example.otpapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.otpapp.ui.theme.GenZBlack
import com.example.otpapp.ui.theme.GenZBlue
import com.example.otpapp.ui.theme.GenZNeon

@Composable
fun LoginScreen(onSendOtp: (String) -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    val isEmailValid = email.contains("@") && email.contains(".")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GenZBlack)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Welcome.",
                style = MaterialTheme.typography.h4,
                color = Color.White
            )
            Text(
                text = "Secure passwordless login.",
                style = MaterialTheme.typography.body1,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = GenZNeon,
                    unfocusedIndicatorColor = Color.DarkGray,
                    textColor = Color.White,
                    cursorColor = GenZNeon
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { if (isEmailValid) onSendOtp(email) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isEmailValid) GenZNeon else Color.DarkGray,
                    disabledBackgroundColor = Color.DarkGray
                ),
                enabled = isEmailValid
            ) {
                Text(
                    text = "GET OTP",
                    style = MaterialTheme.typography.button,
                    color = if (isEmailValid) Color.Black else Color.Gray
                )
            }
        }
    }
}
