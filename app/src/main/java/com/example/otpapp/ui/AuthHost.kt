package com.example.otpapp.ui

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import com.example.otpapp.viewmodel.AuthViewModel
import com.example.otpapp.viewmodel.UiState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthHost(uiState: UiState, viewModel: AuthViewModel) {
    // Keying by the class/type name ensures animations only trigger when switching screens,
    // preventing the "fluctuation" caused by timer updates within the same state.
    AnimatedContent(
        targetState = uiState,
        transitionSpec = {
            val isEnteringSubScreen = targetState is UiState.Session || targetState is UiState.Otp
            if (isEnteringSubScreen) {
                slideInHorizontally { width -> width } + fadeIn() with
                        slideOutHorizontally { width -> -width } + fadeOut()
            } else {
                slideInHorizontally { width -> -width } + fadeIn() with
                        slideOutHorizontally { width -> width } + fadeOut()
            } using SizeTransform(clip = false)
        },
        contentKey = { it::class.java.name }
    ) { targetState ->
        when (targetState) {
            is UiState.Login -> LoginScreen(onSendOtp = { viewModel.sendOtp(it) })
            is UiState.Otp -> OtpScreen(
                state = targetState,
                onVerify = { viewModel.verifyOtp(targetState.email, it) },
                onResend = { viewModel.resendOtp(targetState.email) }
            )
            is UiState.Session -> SessionScreen(state = targetState, onLogout = { viewModel.logout() })
        }
    }
}
