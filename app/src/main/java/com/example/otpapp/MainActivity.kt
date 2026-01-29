package com.example.otpapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.otpapp.ui.AuthHost
import com.example.otpapp.viewmodel.AuthViewModel
import com.example.otpapp.ui.theme.OTPComposeSampleTheme

class MainActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OTPComposeSampleTheme {
                val uiState by viewModel.uiState.collectAsState()
                AuthHost(uiState = uiState, viewModel = viewModel)
            }
        }
    }
}
