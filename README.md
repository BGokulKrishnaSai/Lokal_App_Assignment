# OTP Compose Sample ✅

This is a small Android app demonstrating a passwordless Email + OTP flow built with Kotlin + Jetpack Compose.

Highlights
- OTP rules: 6 digits, expires in 60 seconds, max 3 attempts, resending invalidates previous OTP and resets attempts
- Session screen with live duration mm:ss that survives recomposition
- Integration with Timber for logging analytics events

Run
1. Open the project in Android Studio (2022.3 or newer recommended)
2. Build & Run on an emulator or device

Events logged (via Timber):
- OTP_GENERATED
- OTP_VALIDATION_SUCCESS
- OTP_VALIDATION_FAILURE
- LOGOUT

Notes:
- App stores OTP data per email locally using `OtpManager`.
- Session timer is implemented in `AuthViewModel` and survives recomposition/rotation since it's in ViewModel.

What to look for
- `OtpManager` implements OTP generation, expiry, attempts, and per-email storage
- `AuthViewModel` contains business logic and exposes UI states
- Compose screens: `LoginScreen`, `OtpScreen`, `SessionScreen`

See `docs/DETAILS.md` for design reasoning and notes on what was implemented with GPT assistance.

What I used GPT for vs implemented myself
- Used GPT: quick architecture suggestions, Compose patterns, and phrasing for README.
- Implemented myself: `OtpManager`, `AuthViewModel`, session timer, Compose UI screens and integration with Timber.
