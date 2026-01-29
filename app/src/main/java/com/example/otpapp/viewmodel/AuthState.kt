package com.example.otpapp.viewmodel

sealed interface UiState {
    object Login : UiState
    data class Otp(
        val email: String,
        val secondsLeft: Long = 60,
        val attemptsLeft: Int = 3,
        val error: String? = null
    ) : UiState
    data class Session(
        val email: String,
        val startTimeMillis: Long,
        val elapsedMillis: Long
    ) : UiState
}
