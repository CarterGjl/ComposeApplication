package com.example.composeapplication.ui.bottom

import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.composeapplication.Screen
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi

val items = listOf(
    Screen.Article,
    Screen.Picture,
    Screen.Weather,
    Screen.Test
)

@ExperimentalPagerApi
@Composable
fun BottomNavigationAlwaysShowLabelComponent(
    navController: NavHostController
) {
    val insets = LocalWindowInsets.current
    // 切记，这些信息都是px单位，使用时要根据需求转换单位
    val imeBottom = with(LocalDensity.current) { insets.navigationBars.bottom.toDp() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation(Modifier.height(height = 56.dp + imeBottom)) {
        items.forEachIndexed { _, screen ->
            BottomNavigationItem(
                icon = {
                    Icon(screen.icon, contentDescription = screen.route)
                },
                label = {
                    Text(
                        text = stringResource(id = screen.resourceId),
                    )
                },
                modifier = Modifier.navigationBarsPadding(),
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    if (currentDestination?.route == screen.route) {
                        return@BottomNavigationItem
                    }
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                })
        }
    }

}

//val navBackStackEntry by navController.currentBackStackEntryAsState()
//val currentRoute = navBackStackEntry?.destination?.route
//val routes = remember { items.map { it.route } }
//if (currentRoute in routes) {
//    BottomNavigation {
//        items.forEach { screen ->
//            BottomNavigationItem(
//
//                icon = {
//                    Icon(screen.icon, contentDescription = currentRoute)
//                },
//                label = { Text(text = stringResource(id = screen.resourceId)) },
//                selected = currentRoute == screen.route,
//                onClick = {
//                    if (screen.route == "login") {
//                        navController.navigate("nest") {
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                        return@BottomNavigationItem
//                    }
//                    navController.navigate(screen.route) {
////                                    popUpTo(navController.graph.findStartDestination().id) {
////                                        saveState = true
////                                    }
//                        // Restore state when reselecting a previously selected item
//                        restoreState = true
//                        launchSingleTop = true
//                    }
//                })
//        }
//    }
//}