@file:Suppress("unused")

package com.example.composeapplication.navigation

import androidx.navigation.compose.NamedNavArgument

object NavigationDirections {

    val authentication = object : NavigationCommand {
        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "authentication"
    }

    val dashboard = object : NavigationCommand {
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
        override val destination: String
            get() = "dashboard"
    }
}