package com.example.composeapplication

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composeapplication.bean.Article
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.MineScreen
import com.example.composeapplication.viewmodel.ArticleViewModel
import com.example.composeapplication.viewmodel.search.SearchViewModel
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
fun ArticleScreen(padding: PaddingValues, onClick: (url: String) -> Unit) {
    val articleViewModel: ArticleViewModel = viewModel()
    Log.d(TAG, "ArticleScreen: $padding")
    Column(
        Modifier
            .padding(bottom = 50.dp)
    ) {
        SideEffect {
            articleViewModel.getArticles(0)
        }
        val result = articleViewModel.articles.observeAsState()
        if (result.value != null) {
            ArticalList(result.value!!, onClick)
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

@Composable
private fun ArticalList(
    result: ResultData,
    onClick: (url: String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        val datas = result.data.datas
        items(datas, key = { data ->
            data.id
        }) { data ->
            ArticleItem(data) {
                onClick(it)
            }
//                        ExpandingCard(data) {
//                            onClick(it)
//                        }
        }
//                    item {
//                        Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.Center) {
//                            CircularProgressIndicator(color = Color.Red).also {
//                                Log.d(TAG, "loading: ")
//                            }
//                        }
//                    }

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
            val currentRoute = navBackStackEntry?.destination?.route
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
            Log.d(TAG, "bottomBar: $innerPadding")
            NavHost(
                navController = navController,
                startDestination = Screen.Article.route,
                modifier = Modifier.padding(innerPadding)
            ){
                composable(Screen.Article.route){
                    ArticleScreen(innerPadding){
                        navController.navigate("article_detail?url=$it") {
//                                popUpTo = navController.graph.startDestination
                            launchSingleTop = true
                        }
                    }
                }
                composable(Screen.FriendsList.route) {
                    SearchScreen(modifier = Modifier.padding(innerPadding))
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
fun SearchScreen(viewModel: SearchViewModel = viewModel(),modifier: Modifier) {
    val observeAsState by viewModel.searchResult.observeAsState()
    Column(modifier = modifier.padding(bottom = 50.dp)) {
        SearchContent {
            viewModel.searchArticle(it)
        }
        if (observeAsState != null) {
            ArticalList(result = observeAsState!!) {

            }
        } else {
            Text(text = "empty")

        }
    }

}

@Composable
private fun SearchContent(search: (key: String) -> Unit) {
    var searchKey by remember {
        mutableStateOf("")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = searchKey,
            onValueChange = { searchValue ->
                searchKey = searchValue
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "search",
                    modifier = Modifier.clickable {
                        search(searchKey)
                    }
                )
            },
            keyboardOptions =  KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    search(searchKey)
                }
            ),
            maxLines = 1
        )
    }
}

@Composable
fun ArticleDetailScreen(detailUrl: String) {
    var refreshing by remember { mutableStateOf(true) }
    Column {
        if (refreshing){
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),color = Color.Gray)
        }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                // Sets up
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            refreshing = false
                        }
                    }
                }
            },
            update = { view ->
                Log.d(TAG, "ArticleDetailScreen: ")
                // view 被 inflated
                view.loadUrl(detailUrl)
            })
    }
}

@Composable
fun ArticleItem(data: Article, onClick: (url: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .border(
                width = 1.dp,

                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                ),
                color = Color.Gray
            )
            .clickable {
                onClick(data.link)
            }
            .padding(8.dp)

    ) {
        Text(
            text = data.title,
            style = MaterialTheme.typography.subtitle1
        )
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Icon(imageVector = Icons.Filled.Timeline, contentDescription = "time")
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = data.niceDate,
                style = MaterialTheme.typography.body1
            )
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                style = MaterialTheme.typography.body1,
                color = Color.Red,
                text = data.superChapterName,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
                    .padding(10.dp)
            )
        }
    }
}

@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun MyText() {
    Text(text = "dahfdasjk f")
//    Text(
//        text = "test",
//        modifier = Modifier
//            .border(
//                width = 1.dp,
//                color = Color.Red,
//                shape = RoundedCornerShape(
//                    topStart = 8.dp,
//                    topEnd = 8.dp,
//                    bottomStart = 8.dp,
//                    bottomEnd = 8.dp
//                )
//            )
//            .padding(10.dp)
//    )
}

@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun CardViewPreview() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Gray)
        .wrapContentHeight()
        .padding(top = 8.dp),
        elevation = 8.dp
    ) {
        Text(
            text="This is a Card",
            modifier = Modifier.height(90.dp)
        )
    }
}