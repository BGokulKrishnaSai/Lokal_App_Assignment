package com.example.otpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otpapp.analytics.AnalyticsLogger
import com.example.otpapp.data.OtpManager
import com.example.otpapp.data.OtpValidationResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val otpManager = OtpManager { msg -> AnalyticsLogger.log(msg.split(" ").first(), mapOf("raw" to msg)) }

    private val _uiState = MutableStateFlow<UiState>(UiState.Login)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var sessionJob: Job? = null

    fun sendOtp(email: String) {
        // generate and move to Otp state
        val code = otpManager.generateOtp(email)
        // also log through AnalyticsLogger (human-friendly)
        AnalyticsLogger.logOtpGenerated(email, code)
        _uiState.value = UiState.Otp(email = email, secondsLeft = otpManager.getRemainingSeconds(email), attemptsLeft = otpManager.getAttemptsLeft(email), error = null)
        // start a ticker to update secondsLeft while in OTP state
        startOtpTicker(email)
    }

    private var otpTickerJob: Job? = null

    private fun startOtpTicker(email: String) {
        otpTickerJob?.cancel()
        otpTickerJob = viewModelScope.launch {
            while (true) {
                val seconds = otpManager.getRemainingSeconds(email)
                val attempts = otpManager.getAttemptsLeft(email)
                val current = _uiState.value
                if (current is UiState.Otp && current.email == email) {
                    _uiState.value = current.copy(secondsLeft = seconds, attemptsLeft = attempts)
                }
                if (seconds <= 0) break
                delay(1000)
            }
        }
    }

    fun verifyOtp(email: String, input: String) {
        viewModelScope.launch {
            when (val result = otpManager.validateOtp(email, input)) {
                is OtpValidationResult.Success -> {
                    AnalyticsLogger.logOtpValidationSuccess(email)
                    // start session
                    val start = System.currentTimeMillis()
                    _uiState.value = UiState.Session(email = email, startTimeMillis = start, elapsedMillis = 0L)
                    startSessionTimer(email, start)
                    otpTickerJob?.cancel()
                }
                is OtpValidationResult.Expired -> {
                    AnalyticsLogger.logOtpValidationFailure(email, "expired")
                    _uiState.value = UiState.Otp(email = email, secondsLeft = 0, attemptsLeft = 0, error = "OTP expired")
                }
                is OtpValidationResult.AttemptsExceeded -> {
                    AnalyticsLogger.logOtpValidationFailure(email, "attempts_exceeded")
                    _uiState.value = UiState.Otp(email = email, secondsLeft = 0, attemptsLeft = 0, error = "Attempts exceeded")
                }
                is OtpValidationResult.Incorrect -> {
                    AnalyticsLogger.logOtpValidationFailure(email, "incorrect")
                    val attempts = otpManager.getAttemptsLeft(email)
                    _uiState.value = UiState.Otp(email = email, secondsLeft = otpManager.getRemainingSeconds(email), attemptsLeft = attempts, error = "Incorrect OTP")
                }
                is OtpValidationResult.NoOtp -> {
                    AnalyticsLogger.logOtpValidationFailure(email, "no_otp")
                    _uiState.value = UiState.Login
                }
            }
        }
    }

    fun resendOtp(email: String) {
        val code = otpManager.generateOtp(email)
        AnalyticsLogger.logOtpGenerated(email, code)
        _uiState.value = UiState.Otp(email = email, secondsLeft = otpManager.getRemainingSeconds(email), attemptsLeft = otpManager.getAttemptsLeft(email), error = null)
        startOtpTicker(email)
    }

    private fun startSessionTimer(email: String, startTime: Long) {
        sessionJob?.cancel()
        sessionJob = viewModelScope.launch {
            while (true) {
                val elapsed = System.currentTimeMillis() - startTime
                val current = _uiState.value
                if (current is UiState.Session && current.email == email) {
                    _uiState.value = current.copy(elapsedMillis = elapsed)
                }
                delay(1000)
            }
        }
    }

    fun logout() {
        val current = _uiState.value
        if (current is UiState.Session) {
            AnalyticsLogger.logLogout(current.email)
        }
        sessionJob?.cancel()
        otpTickerJob?.cancel()
        _uiState.value = UiState.Login
    }
}
