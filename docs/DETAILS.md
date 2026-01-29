# Design Details

## OTP logic & expiry handling
- OTP is a 6-digit numeric string generated with padding to allow leading zeros.
- Stored per-email in a ConcurrentHashMap with a createdAt timestamp and remaining attempts.
- Expiry is enforced by comparing System.currentTimeMillis() with createdAt + 60s.
- On validation, expired OTPs are removed and reported as expired.
- Incorrect attempts decrement attemptsLeft; when attempts reach 0 the OTP is removed and attempts-exceeded is returned.
- Resending generates a new OTP, which replaces the previous entry and resets attempts.

## Data structures used
- ConcurrentHashMap<String, OtpEntry> for per-email storage to allow thread-safe operations.
- Mutex-protected critical sections for validate operation to ensure atomic updates to attempts.
- Sealed UI states (`UiState`) for clear one-way flow between screens.

## External SDK
- Chose `Timber` for lightweight logging/analytics during development.
- Initialized in `App` and logged key events in `AnalyticsLogger`.

## GPT usage
- I used GPT to help refine architecture and compose some boilerplate UI patterns but implemented the core OTP logic, ViewModel flows, and session timer logic myself.

