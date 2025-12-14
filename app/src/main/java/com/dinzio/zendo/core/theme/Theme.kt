package com.dinzio.zendo.core.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    onPrimary = White,
    secondary = GreenAccent,
    onSecondary = BlackText,
    tertiary = OrangeAccent,
    onTertiary = White,
    background = BackgroundBlack,
    onBackground = White,
    surface = BackgroundBlack,
    onSurface = White,
    surfaceVariant = BlackText,
    onSurfaceVariant = GrayText,
    error = RedDelete,
    onError = White
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = White,
    secondary = GreenSecondary,
    onSecondary = BlackText,
    tertiary = OrangeAccent,
    onTertiary = White,
    background = White,
    onBackground = BlackText,
    surface = White,
    onSurface = BlackText,
    surfaceVariant = BackgroundGray,
    onSurfaceVariant = GrayText,
    error = RedDelete,
    onError = White
)

@Composable
fun ZendoTheme(
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