package com.example.composeapplication.ui.screen


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composeapplication.AppRuntime
import com.example.composeapplication.LocalSnackbarHostState
import com.example.composeapplication.Screen
import com.example.composeapplication.extend.ProvideNavHostController
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.LoginScreen
import com.example.composeapplication.ui.MineScreen
import com.example.composeapplication.ui.bottom.BottomNavigationAlwaysShowLabelComponent
import com.example.composeapplication.ui.screen.type.TypeContentScreen
import com.example.composeapplication.ui.screen.type.TypeScreen
import com.example.composeapplication.ui.screen.type.bean.TreeListResponse
import com.example.composeapplication.ui.weather.WeatherPage
import com.example.composeapplication.viewmodel.MainViewModel
//import com.google.accompanist.navigation.animation.composable

//import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@Composable
fun MainPage(viewModel: MainViewModel = viewModel()) {

    val navController = rememberNavController()

    AppRuntime.rememberScaffoldState = LocalSnackbarHostState.current
    viewModel.setNavControllerA(navController)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            when (currentDestination?.route) {
                Screen.Article.route,
                Screen.TypeTree.route -> {
                    BottomNavigationAlwaysShowLabelComponent(navController)
                }
            }

        },
        floatingActionButton = {
            IconButton(onClick = {
                navController.navigate("camera")
            }) {
                Icon(imageVector = Icons.Filled.Camera, contentDescription = "camera")
            }
        },
    ) {
        ProvideNavHostController(navHostController = navController) {
            NavHost(
//                modifier = Modifier.padding(it),
                navController = navController,
                startDestination = Screen.Article.route
            ) {
                mycomposable(Screen.Article.route) {
                    ArticleScreen(navController = navController) { url, title ->
                        val encode = URLEncoder.encode(url, "utf-8")
                        navController.navigate(Screen.WebView.route + "?title=$title&url=$encode") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
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
                    SearchScreen(mainViewModel = viewModel, navController = navController)
                }
                mycomposable("camera") {
                    val current = LocalContext.current
                    FeatureThatRequiresCameraPermission(navigateToSettingsScreen = {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", current.packageName, null)
                            }
                        current.startActivity(intent)
                    }) {
                        CameraPreview()
                    }
                }
                composable(Screen.TypeTree.route) {
                    TypeScreen { knowledge ->
                        val toJson = Gson().toJson(knowledge)
                        navController.navigate("type_content?knowledge=$toJson") {
                            popUpTo(navController.graph[Screen.TypeTree.route].id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
                composable("type_content?knowledge={knowledge}") { backStackEntry ->
                    val knowledge: String = backStackEntry.arguments?.getString("knowledge") ?: ""
                    val fromJson =
                        Gson().fromJson(knowledge, TreeListResponse.Knowledge::class.java)
                    TypeContentScreen(knowledge = fromJson)
                }
                composable(Screen.Music.route) {
//                    VideoScreen()
                    PlayerPage()
                }
                navigation("login", Screen.Mine.route) {
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("mine_page") {
                        MineScreen()
                    }
                }
                composable(
                    route = Screen.WebView.route + "?title={title}&url={url}",
                    arguments = listOf(
                        navArgument("title") { defaultValue = "" },
                        navArgument("url") { defaultValue = "" }
                    ),
                ) { backStackEntry ->

                    val title: String = backStackEntry.arguments?.getString("title") ?: ""
                    val url: String = backStackEntry.arguments?.getString("url") ?: ""
                    val decode = URLDecoder.decode(url, "utf-8")
                    ArticleDetailScreen(detailUrl = decode, title) {
                        navController.popBackStack()
                    }
                }

            }
        }
    }
}

@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class,
)
@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun MainPagePreview() {
    ComposeApplicationTheme {
        MainPage()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(navController: NavHostController, mainViewModel: MainViewModel = viewModel()) {
    val observeAsState by mainViewModel.titleId.observeAsState(Screen.Article.resourceId)
    Column {
        Spacer(
            modifier = Modifier
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
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
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        )
    }
}

fun NavGraphBuilder.mycomposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    return composable(
        route = route,
        content = content,
        arguments = arguments,
        deepLinks = deepLinks,
//        enterTransition = {
//            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
//        },
//        exitTransition = {
//            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
//        }
    )
}
