package com.example.composeapplication.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
        primary = colorPrimaryDark1,
        primaryVariant = colorPrimaryDark2,
        secondary = teal200,
        onPrimary = white,
)

private val LightColorPalette = lightColors(
    primary = green300,
    primaryVariant = green300,
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
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
    )
}