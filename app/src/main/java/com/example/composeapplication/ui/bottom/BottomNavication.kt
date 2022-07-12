package com.example.composeapplication.ui.bottom

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import com.google.accompanist.pager.ExperimentalPagerApi

val items = listOf(
    Screen.Article,
    Screen.TypeTree,
    Screen.Weather,
    Screen.Mine
)

@ExperimentalPagerApi
@Composable
fun BottomNavigationAlwaysShowLabelComponent(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation(Modifier.height(80.dp)) {
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
