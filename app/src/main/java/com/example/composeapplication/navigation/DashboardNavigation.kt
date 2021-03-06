@file:Suppress("unused")

package com.example.composeapplication.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object DashboardNavigation {

    private const val KEY_USERID = "userId"

    const val route = "dashboard/${KEY_USERID}"
    val arguments = listOf(
        navArgument(KEY_USERID){
            type = NavType.StringType
        }
    )

    fun dashboard(userId: String? = null) = object : NavigationCommand {
        override val arguments = this@DashboardNavigation.arguments
        override val destination = "dashboard/$userId"
    }
}