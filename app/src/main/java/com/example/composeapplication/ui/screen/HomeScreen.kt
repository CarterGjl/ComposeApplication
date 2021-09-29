package com.example.composeapplication.ui.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.*
import com.example.composeapplication.ui.MineScreen
import com.google.accompanist.pager.ExperimentalPagerApi

private const val TAG = "HomeScreen"

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    Scaffold(topBar = {},
        content = { innerPadding ->
            Log.d(TAG, "bottomBar: $innerPadding")
            NavHost(
                navController = navController,
                startDestination = Screen.Article.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Article.route) {
                    Log.d(TAG, "ArticleScreen: ")
                    ArticleScreen { url, _ ->
                        navController.navigate("article_detail?url=$url") {
                            launchSingleTop = true
                        }
                    }
                }
                composable(Screen.FriendsList.route) {
                    Log.d(TAG, "SearchScreen: ")
                    PicturePage()
//                    SearchScreen()
                }
//                composable(Screen.Login.route) {
//                    MineScreen()
//                }
                navigation(startDestination = Screen.Login.route, NEST) {
                    composable(Screen.Login.route) {
                        MineScreen(navController = navController)
                    }
                    composable(MINE) {
                        Text(text = "success")
                    }
                }
                composable(Screen.ArticleDetail.route) {
                    val url = it.arguments?.getString("url")
                    url?.let { detailUrl ->
                        ArticleDetailScreen(
                            detailUrl = detailUrl,
                        )
                    }
                }
                dialog(DIALOG) {
                    Dialog(onDismissRequest = {
                        Log.d(TAG, "onDismissRequest: ")
                    }) {

                    }
                }
            }

        }
    )
}