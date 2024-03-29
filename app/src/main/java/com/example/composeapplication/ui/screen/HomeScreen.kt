package com.example.composeapplication.ui.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.composeapplication.*
import com.example.composeapplication.ui.MineScreen

private const val TAG = "HomeScreen"

@ExperimentalFoundationApi
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
                    ArticleScreen(navController = navController) { url, _ ->
                        navController.navigate("article_detail?url=$url") {
                            launchSingleTop = true
                        }
                    }
                }
                composable(Screen.Picture.route) {
                    Log.d(TAG, "SearchScreen: ")
                    PicturePage()
//                    SearchScreen()
                }
//                composable(Screen.Login.route) {
//                    MineScreen()
//                }
                navigation(startDestination = Screen.Weather.route, NEST) {
                    composable(Screen.Weather.route) {
                        MineScreen()
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