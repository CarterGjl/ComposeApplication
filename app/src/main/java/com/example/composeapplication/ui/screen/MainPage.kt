package com.example.composeapplication.ui.screen


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.Screen
import com.example.composeapplication.ui.bottom.BottomNavigationAlwaysShowLabelComponent
import com.example.composeapplication.ui.weather.WeatherPage
import com.example.composeapplication.viewmodel.MainViewModel
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@OptIn(ExperimentalAnimationApi::class)
@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun MainPage(viewModel: MainViewModel = viewModel()) {

    val navController = rememberAnimatedNavController()
    viewModel.setNavControllerA(navController)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        topBar = {
            when (currentDestination?.route) {
                Screen.Article.route,
                Screen.Picture.route -> {
                    AppBar(navController)
                }
            }
        },
        bottomBar = {
            when (currentDestination?.route) {
                Screen.Article.route,
                Screen.Picture.route -> {
                    BottomNavigationAlwaysShowLabelComponent(navController)
                }
            }

        },
        floatingActionButton = {
            IconButton(onClick = {
                viewModel.navigateToCameraPage()
            }) {
                Icon(imageVector = Icons.Filled.Camera, contentDescription = "camera")
            }
        }
    ) {
        AnimatedNavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = Screen.Article.route
        ) {
            mycomposable(Screen.Article.route) {
                ArticleScreen { url, title ->
                    viewModel.navigateToWebViewPage(title = title, url = url)
                }
            }
            mycomposable(Screen.Picture.route) {
                PicturePage { url ->
                    viewModel.navigateToPhotoPage(url = url)
                }
            }
            mycomposable(Screen.Weather.route) {
                WeatherPage()
            }
            mycomposable(Screen.Test.route) {
                TestPage()
            }
            mycomposable(Screen.Search.route) {
                SearchScreen(mainViewModel = viewModel)
            }

        }
    }
}

@Composable
private fun AppBar(navController: NavHostController, mainViewModel: MainViewModel = viewModel()) {
    val observeAsState by mainViewModel.titleId.observeAsState(Screen.Article.resourceId)
    Column {
        Spacer(
            modifier = Modifier
                .statusBarsHeight()
                .fillMaxWidth()
                .background(MaterialTheme.colors.primaryVariant)
        )
        TopAppBar(
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Share, contentDescription = "share")
                }
                IconButton(onClick = {
                    navController.navigate(Screen.Search.route)
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "share")
                }
            },
            title = {
                Text(
                    text = stringResource(id = observeAsState),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onPrimary
                )
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            elevation = 0.dp
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mycomposable(route:String, content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit){
    return composable(
        route = route,
        content = content,
//        enterTransition = {
//            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
//        },
//        exitTransition = {
//            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
//        }
    )
}
