package com.example.composeapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.composeapplication.bean.Article
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.MineScreen
import com.example.composeapplication.ui.ShowDialog
import com.example.composeapplication.viewmodel.ArticleViewModel
import com.google.accompanist.coil.CoilImage
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets {
                ComposeApplicationTheme {
                    HomeScreen()
                }
            }
        }
    }
}
@Composable
fun ExpandingCard(title: String, body: String, data: Article,onClick:(url:String)->Unit) {
    var expanded by remember { mutableStateOf(false) }

    // describe the card for the current state of expanded
    Card(modifier = Modifier
        .background(color = Color.Gray)
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(top = 8.dp)
        .clickable {
            onClick(data.projectLink)
        }) {
        Column(
            Modifier
                .width(280.dp)
                .animateContentSize() // automatically animate size when it changes
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            CoilImage(
                data = data.envelopePic,
                contentDescription = "description of the image"
            )
            Text(text = title)

            // content of the card depends on the current value of expanded
            if (expanded) {
                Text(text = body, Modifier.padding(top = 8.dp))
                // change expanded in response to click events
                IconButton(onClick = { expanded = false }, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.ExpandLess, contentDescription = "Expand less")
                }
            } else {
                // change expanded in response to click events
                IconButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.ExpandMore, contentDescription = "Expand more")
                }
            }
        }
    }
}
@Composable
fun PasswordTextField(string: String?) {
    val articleViewModel: ArticleViewModel = viewModel()

    var password by remember {
        if (string == null) {
            mutableStateOf("")
        } else {
            mutableStateOf(string)
        }
    }

    Column {
        TextField(
            value = password,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                password = it
                articleViewModel.getArticles(0)
            },
            label = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        val openDialog = remember { mutableStateOf(false) }
        Button(onClick = { openDialog.value = true }) {
            Text("Click me")
        }
        ShowDialog(R.string.app_name, R.string.about_content, openDialog)
    }
}

@Composable
fun ArticleScreen(modifier: Modifier,onClick: (url: String) -> Unit) {
    val articleViewModel: ArticleViewModel = viewModel()
    Column(
        modifier
            .fillMaxWidth()
            .background(color = Color.Gray)
    ) {
        SideEffect {
            articleViewModel.getArticles(0)
        }
        val result = articleViewModel.movies.observeAsState()
        if (result.value != null) {
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                if (result.value != null) {
                    val datas = result.value!!.data.datas
                    items(datas, key = { data ->
                        data.id
                    }) { data ->
                        ExpandingCard(title = data.title, body = data.desc, data) {
                            onClick(it)
                        }
                    }
                    item {
                        Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.Center) {
                            CircularProgressIndicator().also {
                                Log.d(TAG, "loading: ")
                            }
                        }
                    }
                }

            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.Gray)
            ) {
                Text(text = "empty")
            }
        }
    }

}

@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun BoxScope() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(text = "empty")
    }
}


@Composable
fun HomeScreen() {
    val items = listOf(
        Screen.Article,
        Screen.FriendsList,
        Screen.Login,
    )
    val navController = rememberNavController()
    Scaffold(topBar = {
        Column {
            Spacer(
                modifier = Modifier
                    .statusBarsHeight()
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primaryVariant)
            )
            TopAppBar(
                title = {
                    Text(text = "compose demo")
                },
                backgroundColor = MaterialTheme.colors.primaryVariant,
                elevation = 0.dp
            )
        }

    },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
            val routes = remember { items.map { it.route } }
            if (currentRoute in routes){
                BottomNavigation {


                    items.forEach { screen ->
                        BottomNavigationItem(

                            icon = {
                                Icon(screen.icon, contentDescription = currentRoute)
                            },
                            label = { Text(text = stringResource(id = screen.resourceId)) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
//                                popUpTo = navController.graph.startDestination
                                    launchSingleTop = true
                                }
                            })
                    }
                }
            }

        },
        content = { innerPadding->
            NavHost(navController = navController, startDestination = Screen.Article.route){
                composable(Screen.Article.route){
                    ArticleScreen(Modifier.padding(innerPadding)){
                        navController.navigate("article_detail?url=$it") {
//                                popUpTo = navController.graph.startDestination
                            launchSingleTop = true
                        }
                    }
                }
                composable(Screen.FriendsList.route) { backStackEntry ->
                    PasswordTextField(backStackEntry.arguments?.getString("userId"))
                }
                composable(Screen.Login.route){
                    MineScreen()
                }
                composable(Screen.ArticleDetail.route){
                    val url = it.arguments?.getString("url")
                    url?.let { detailUrl -> ArticleDetailScreen(detailUrl = detailUrl) }
                }
            }

        }
    )
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {

    object Article : Screen("article", R.string.article, Icons.Filled.Article)
    object FriendsList : Screen("friendslist", R.string.friends_list, Icons.Filled.Search)
    object Login : Screen("login", R.string.login, Icons.Filled.AccountBox)
    object ArticleDetail : Screen("article_detail?url={url}", R.string.detail, Icons.Filled.AccountBox)
}


@Composable
fun ArticleDetailScreen(detailUrl:String) {
    Text(text = detailUrl)
}