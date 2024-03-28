package com.example.composeapplication.ui.bottom

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.composeapplication.Screen
import com.example.composeapplication.viewmodel.MainViewModel

val items = listOf(
    Screen.Article,
    Screen.TypeTree,
//    Screen.Weather,
    Screen.Mine,
    Screen.Music
)

@Composable
fun BottomNavigationAlwaysShowLabelComponent(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        Modifier
            .height(
                80.dp +
                        WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
            )
    ) {
        items.forEachIndexed { _, screen ->
            NavigationBarItem(
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
                        return@NavigationBarItem
                    }
                    mainViewModel.showTitle(screen.resourceId)
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
