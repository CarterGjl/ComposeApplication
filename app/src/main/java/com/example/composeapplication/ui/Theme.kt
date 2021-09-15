package com.example.composeapplication.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
        primary = black,
        primaryVariant = white,
        secondary = teal200
)

private val LightColorPalette = lightColors(
    primary = black,
    primaryVariant = black,
    secondary = pink900,
    background = white,
    surface = white,
    onPrimary = white,
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
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
    )
}