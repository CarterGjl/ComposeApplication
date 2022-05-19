package com.example.composeapplication.extend

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavHostController = staticCompositionLocalOf<NavHostController> {
    error("CompositionLocal NavHostController not present")
}

@Composable
fun ProvideNavHostController(
    navHostController: NavHostController,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalNavHostController provides navHostController) {
        content()
    }
}