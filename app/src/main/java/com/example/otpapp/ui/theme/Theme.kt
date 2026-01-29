package com.example.otpapp.ui.theme

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = GenZNeon,
    primaryVariant = GenZBlue,
    secondary = GenZPink,
    background = GenZBlack,
    surface = GenZSurface,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun OTPComposeSampleTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
