package com.example.finad.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// Color palette
val Emerald = Color(0xFF2ECC71)
val EmeraldDark = Color(0xFF27AE60)
val Slate = Color(0xFF2F3E46)
val SlateDark = Color(0xFF263238)
val BackgroundDark = Color(0xFF121212)
val SurfaceDark = Color(0xFF1E1E1E)

private val LightColors = lightColorScheme(
    primary = Slate,
    onPrimary = Color.White,
    secondary = Emerald,
    onSecondary = Color.White,
    background = Color(0xFFF9FAFB),
    onBackground = Color(0xFF1C1C1C),
    surface = Color.White,
    onSurface = Color(0xFF1C1C1C),
)

private val DarkColors = darkColorScheme(
    primary = SlateDark,
    onPrimary = Color.White,
    secondary = EmeraldDark,
    onSecondary = Color.White,
    background = BackgroundDark,
    onBackground = Color(0xFFE0E0E0),
    surface = SurfaceDark,
    onSurface = Color(0xFFE0E0E0),
)

@Composable
fun FinadTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
