package com.example.islamicdailycompanion.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🌿 Light Green Theme

private val LightColorScheme = lightColorScheme(

    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,

    secondary = Color(0xFF1B5E20),
    onSecondary = Color.White,

    tertiary = Color(0xFF388E3C),
    onTertiary = Color.White,

    background = Color(0xFFE8F5E9),
    onBackground = Color(0xFF16351A),

    surface = Color.White,
    onSurface = Color(0xFF16351A),

    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF1B5E20)
)

// 🌙 Dark Theme

private val DarkColorScheme = darkColorScheme(

    primary = Color(0xFF81C784),
    onPrimary = Color(0xFF003300),

    secondary = Color(0xFFA5D6A7),
    onSecondary = Color(0xFF003300),

    tertiary = Color(0xFF81C784),
    onTertiary = Color(0xFF003300),

    background = Color(0xFF102012),
    onBackground = Color.White,

    surface = Color(0xFF182B1A),
    onSurface = Color.White,

    primaryContainer = Color(0xFF2E7D32),
    onPrimaryContainer = Color.White
)

@Composable
fun IslamicDailyCompanionTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}