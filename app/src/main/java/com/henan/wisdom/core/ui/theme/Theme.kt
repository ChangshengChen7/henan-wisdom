package com.henan.wisdom.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryGreen.copy(alpha = 0.1f),
    onPrimaryContainer = PrimaryGreen,
    secondary = PrimaryGold,
    onSecondary = OnPrimaryDark,
    secondaryContainer = PrimaryGold.copy(alpha = 0.1f),
    onSecondaryContainer = PrimaryGold,
    tertiary = PrimaryOrange,
    onTertiary = OnPrimaryDark,
    tertiaryContainer = PrimaryOrange.copy(alpha = 0.1f),
    onTertiaryContainer = PrimaryOrange,
    error = ErrorRed,
    onError = OnPrimaryLight,
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onErrorContainer = ErrorRed,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextSecondary,
    outline = TextSecondary
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryGreen.copy(alpha = 0.2f),
    onPrimaryContainer = PrimaryGreen,
    secondary = PrimaryGold,
    onSecondary = OnPrimaryDark,
    secondaryContainer = PrimaryGold.copy(alpha = 0.2f),
    onSecondaryContainer = PrimaryGold,
    tertiary = PrimaryOrange,
    onTertiary = OnPrimaryDark,
    tertiaryContainer = PrimaryOrange.copy(alpha = 0.2f),
    onTertiaryContainer = PrimaryOrange,
    error = ErrorRed,
    onError = OnPrimaryLight,
    errorContainer = ErrorRed.copy(alpha = 0.2f),
    onErrorContainer = ErrorRed,
    background = BackgroundDark,
    onBackground = OnPrimaryLight,
    surface = SurfaceDark,
    onSurface = OnPrimaryLight,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = OnPrimaryLight.copy(alpha = 0.7f),
    outline = OnPrimaryLight.copy(alpha = 0.5f)
)

@Composable
fun SmarterHenanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
