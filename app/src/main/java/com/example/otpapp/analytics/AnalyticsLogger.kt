package com.example.otpapp.analytics

import timber.log.Timber

object AnalyticsLogger {
    private val tag = "Analytics"

    fun log(event: String, payload: Map<String, String> = emptyMap()) {
        val details = payload.entries.joinToString(",") { "${it.key}=${it.value}" }
        Timber.tag(tag).i("$event $details")
    }

    fun logOtpGenerated(email: String, code: String) = log("OTP_GENERATED", mapOf("email" to email, "code" to code))
    fun logOtpValidationSuccess(email: String) = log("OTP_VALIDATION_SUCCESS", mapOf("email" to email))
    fun logOtpValidationFailure(email: String, reason: String) = log("OTP_VALIDATION_FAILURE", mapOf("email" to email, "reason" to reason))
    fun logLogout(email: String) = log("LOGOUT", mapOf("email" to email))
}
