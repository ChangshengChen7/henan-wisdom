package com.henan.wisdom.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = PrimaryGold,
    tertiary = PrimaryOrange,
    background = BackgroundLight,
    surface = SurfaceLight,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    secondary = PrimaryGold,
    tertiary = PrimaryOrange,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorRed
)

@Composable
fun HenanWisdomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
