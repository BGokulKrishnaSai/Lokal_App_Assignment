package com.example.otpapp.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

private const val OTP_LENGTH = 6
private const val OTP_EXPIRY_MS = 60_000L
private const val MAX_ATTEMPTS = 3

sealed class OtpValidationResult {
    object Success : OtpValidationResult()
    object Expired : OtpValidationResult()
    object Incorrect : OtpValidationResult()
    object AttemptsExceeded : OtpValidationResult()
    object NoOtp : OtpValidationResult()
}

private data class OtpEntry(
    val code: String,
    val createdAt: Long,
    var attemptsLeft: Int
)

class OtpManager(private val logger: (String) -> Unit = { Timber.tag("Analytics").i(it) }) {
    private val data = ConcurrentHashMap<String, OtpEntry>()
    private val mutex = Mutex()

    fun generateOtp(email: String): String {
        val code = (0..999_999).random().toString().padStart(OTP_LENGTH, '0')
        val entry = OtpEntry(code, System.currentTimeMillis(), MAX_ATTEMPTS)
        data[email] = entry
        logger("OTP_GENERATED email=$email code=$code")
        return code
    }

    suspend fun validateOtp(email: String, input: String): OtpValidationResult {
        return mutex.withLock {
            val entry = data[email] ?: return OtpValidationResult.NoOtp
            val now = System.currentTimeMillis()
            if (now - entry.createdAt > OTP_EXPIRY_MS) {
                data.remove(email)
                logger("OTP_EXPIRED email=$email code=${entry.code}")
                return OtpValidationResult.Expired
            }
            if (entry.attemptsLeft <= 0) {
                data.remove(email)
                logger("OTP_ATTEMPTS_EXCEEDED email=$email")
                return OtpValidationResult.AttemptsExceeded
            }
            if (entry.code == input) {
                data.remove(email)
                logger("OTP_VALIDATION_SUCCESS email=$email")
                return OtpValidationResult.Success
            } else {
                entry.attemptsLeft = entry.attemptsLeft - 1
                logger("OTP_VALIDATION_FAILURE email=$email attemptsLeft=${entry.attemptsLeft}")
                if (entry.attemptsLeft <= 0) {
                    data.remove(email)
                    return OtpValidationResult.AttemptsExceeded
                }
                return OtpValidationResult.Incorrect
            }
        }
    }

    fun getRemainingSeconds(email: String): Long {
        val entry = data[email] ?: return 0L
        val remaining = OTP_EXPIRY_MS - (System.currentTimeMillis() - entry.createdAt)
        return if (remaining > 0) (remaining / 1000) else 0L
    }

    fun getAttemptsLeft(email: String): Int {
        return data[email]?.attemptsLeft ?: 0
    }

    // For tests / debugging
    fun peekOtp(email: String): String? = data[email]?.code
}
