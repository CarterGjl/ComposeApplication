package com.example.composeapplication.navigation

import androidx.compose.runtime.mutableStateOf

class NavigationManger {

    var commands = mutableStateOf(DashboardNavigation.dashboard())

    fun navigate(directions: NavigationCommand) {
        commands.value = directions
    }
}