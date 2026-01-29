# OTP Compose Sample ✅

This is a small Android app demonstrating a passwordless Email + OTP flow built with Kotlin + Jetpack Compose.

Highlights
- OTP rules: 6 digits, expires in 60 seconds, max 3 attempts, resending invalidates previous OTP and resets attempts
- Session screen with live duration mm:ss that survives recomposition
- Integration with Timber for logging analytics events

Run
1. Open the project in Android Studio
2. Build & Run on an emulator or device

<img width="2560" height="1440" alt="Screenshot (184)" src="https://github.com/user-attachments/assets/980020dc-7b75-4893-8b23-058f243c0212" />
<img width="2560" height="1440" alt="Screenshot (185)" src="https://github.com/user-attachments/assets/3b3c3bdc-4e19-4ee3-b75d-8baef97ea0f4" />

Events logged (via Timber):
- OTP_GENERATED
- OTP_VALIDATION_SUCCESS
- OTP_VALIDATION_FAILURE
- LOGOUT
<img width="2560" height="1440" alt="Screenshot (186)" src="https://github.com/user-attachments/assets/e18c13d2-611f-49e0-9694-c66f88507332" />
<img width="2560" height="1440" alt="Screenshot (187)" src="https://github.com/user-attachments/assets/73a21dbb-43a8-4f82-bf6c-fc398a3d98da" />

Notes:
- App stores OTP data per email locally using `OtpManager`.
- Session timer is implemented in `AuthViewModel` and survives recomposition/rotation since it's in ViewModel.

What to look for
- `OtpManager` implements OTP generation, expiry, attempts, and per-email storage
- `AuthViewModel` contains business logic and exposes UI states
- Compose screens: `LoginScreen`, `OtpScreen`, `SessionScreen`
- Implemented myself: `OtpManager`, `AuthViewModel`, session timer, Compose UI screens and integration with Timber.
