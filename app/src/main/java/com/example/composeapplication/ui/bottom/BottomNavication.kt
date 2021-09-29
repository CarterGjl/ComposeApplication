package com.example.composeapplication.ui.bottom

import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composeapplication.Screen
import com.example.composeapplication.viewmodel.MainViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

val items = listOf(
    Screen.Article,
    Screen.FriendsList,
    Screen.Login,
    Screen.Test
)

@ExperimentalPagerApi
@Composable
fun BottomNavigationAlwaysShowLabelComponent(
    pagerState: PagerState,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val selectedIndex by viewModel.getSelectedIndex().observeAsState()
    val rememberCoroutineScope = rememberCoroutineScope()
    val insets = LocalWindowInsets.current
    // 切记，这些信息都是px单位，使用时要根据需求转换单位
    val imeBottom = with(LocalDensity.current) { insets.navigationBars.bottom.toDp() }

    BottomNavigation(Modifier.height(height = 56.dp + imeBottom)) {
        items.forEachIndexed { index, screen ->
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
                selected = selectedIndex == index,
                onClick = {
                    viewModel.saveSelectIndex(index)
                    rememberCoroutineScope.launch {
                        pagerState.scrollToPage(index)
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