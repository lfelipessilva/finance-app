package com.example.finad.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Enhanced color palette with better dark mode support
val Emerald = Color(0xFF2ECC71)
val EmeraldDark = Color(0xFF27AE60)
val EmeraldLight = Color(0xFF4CAF50)
val Slate = Color(0xFF2F3E46)
val SlateDark = Color(0xFF263238)
val SlateLight = Color(0xFF546E7A)

// Improved dark mode colors
val BackgroundDark = Color(0xFF0A0A0A) // Darker background for better contrast
val SurfaceDark = Color(0xFF1A1A1A) // Slightly lighter surface
val SurfaceVariantDark = Color(0xFF2A2A2A) // For cards and elevated surfaces
val OnSurfaceDark = Color(0xFFE8E8E8) // Better text contrast
val OnSurfaceVariantDark = Color(0xFFB0B0B0) // Secondary text
val OutlineDark = Color(0xFF404040) // Better outline visibility
val PrimaryDark = Color(0xFF4FC3F7) // Brighter primary for better visibility
val SecondaryDark = Color(0xFF81C784) // Softer secondary color

private val LightColors =
        lightColorScheme(
                primary = Slate,
                onPrimary = Color.White,
                primaryContainer = Color(0xFFE3F2FD),
                onPrimaryContainer = Color(0xFF0D47A1),
                secondary = Emerald,
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFE8F5E8),
                onSecondaryContainer = Color(0xFF1B5E20),
                background = Color(0xFFF9FAFB),
                onBackground = Color(0xFF1C1C1C),
                surface = Color.White,
                onSurface = Color(0xFF1C1C1C),
                surfaceVariant = Color(0xFFF5F5F5),
                onSurfaceVariant = Color(0xFF666666),
                outline = Color(0xFFE0E0E0),
                outlineVariant = Color(0xFFCCCCCC),
                error = Color(0xFFD32F2F),
                onError = Color.White,
                errorContainer = Color(0xFFFFEBEE),
                onErrorContainer = Color(0xFFB71C1C),
        )

private val DarkColors =
        darkColorScheme(
                primary = PrimaryDark,
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF1A237E),
                onPrimaryContainer = Color(0xFFBBDEFB),
                secondary = SecondaryDark,
                onSecondary = Color.Black,
                secondaryContainer = Color(0xFF1B5E20),
                onSecondaryContainer = Color(0xFFC8E6C9),
                background = BackgroundDark,
                onBackground = OnSurfaceDark,
                surface = SurfaceDark,
                onSurface = OnSurfaceDark,
                surfaceVariant = SurfaceVariantDark,
                onSurfaceVariant = OnSurfaceVariantDark,
                outline = OutlineDark,
                outlineVariant = Color(0xFF606060),
                error = Color(0xFFF44336),
                onError = Color.Black,
                errorContainer = Color(0xFF4A148C),
                onErrorContainer = Color(0xFFE1BEE7),
        )

@Composable
fun FinadTheme(useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(colorScheme = colors, content = content)
}
