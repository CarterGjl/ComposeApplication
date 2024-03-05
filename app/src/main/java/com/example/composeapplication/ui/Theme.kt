package com.example.composeapplication.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = colorPrimaryDark1,
    primaryContainer = colorPrimaryDark2,
    secondary = teal200,
    onPrimary = white,
    surface = Color.DarkGray
)

private val LightColorPalette = lightColorScheme(
    primary = green300,
    primaryContainer = green300,
    secondary = pink900,
    background = white,
    surface = white,
    onPrimary = black,
    onSecondary = white,
    onBackground = gray,
    onSurface = gray
)


@Composable
fun ComposeApplicationTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colorScheme = colors,
            typography = typography,
            shapes = shapes,
            content = content
    )
}