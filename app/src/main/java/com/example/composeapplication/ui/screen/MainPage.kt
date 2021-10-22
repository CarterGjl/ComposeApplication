package com.example.composeapplication.ui.screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.R
import com.example.composeapplication.Screen
import com.example.composeapplication.ui.bottom.BottomNavigationAlwaysShowLabelComponent
import com.example.composeapplication.ui.weather.WeatherPage
import com.example.composeapplication.viewmodel.MainViewModel
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun MainPage(viewModel: MainViewModel = viewModel()) {

    val navController = rememberNavController()
    Scaffold(
        topBar = {
            Column {
                Spacer(
                    modifier = Modifier
                        .statusBarsHeight()
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primaryVariant)
                )
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onPrimary
                        )
                    },
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    elevation = 0.dp
                )
            }
        },
        bottomBar = {
            BottomNavigationAlwaysShowLabelComponent(navController)
        },
        floatingActionButton = {
            IconButton(onClick = {
                viewModel.navigateToCameraPage()
            }) {
                Icon(imageVector = Icons.Filled.Camera, contentDescription = "camera")
            }
        }
    ) {

        NavHost(navController = navController, startDestination = Screen.Article.route) {
            composable(Screen.Article.route) {
                ArticleScreen { url, title ->
                    viewModel.navigateToWebViewPage(title = title, url = url)
//                    WebViewActivity.go(current as Activity, url, title = title)
                }
            }
            composable(Screen.Picture.route) {
                PicturePage {
                    viewModel.navigateToPhotoPage(url = it)
                }
            }
            composable(Screen.Weather.route) {
                WeatherPage()
            }
            composable(Screen.Test.route) {
                TestPage()
            }

        }
    }
}