package com.example.composeapplication

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.activity.bsae.BaseActivity
import com.example.composeapplication.bean.Article
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.HomeMain
import com.example.composeapplication.ui.MineScreen
import com.example.composeapplication.ui.banner.NewsBanner
import com.example.composeapplication.ui.bottom.BottomNavigationAlwaysShowLabelComponent
import com.example.composeapplication.ui.screen.LoadingPage
import com.example.composeapplication.ui.screen.PicturePage
import com.example.composeapplication.viewmodel.ArticleViewModel
import com.example.composeapplication.viewmodel.BannerViewModel
import com.example.composeapplication.viewmodel.MainViewModel
import com.example.composeapplication.viewmodel.search.SearchViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

// 官方demo地址
// https://github.com/android/compose-samples
private const val TAG = "MainActivity"

class MainActivity : BaseActivity() {

    @ExperimentalFoundationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets {
                ComposeApplicationTheme {
                    HomeMain()
                }
            }
        }
    }
}
val a = test()
fun test(){
    GlobalScope.launch {
        flow {
            emit(1)
            throw ArithmeticException("div 0")
        }.catch {
            Log.d(TAG, "onCreate:catch error $it")
            println("catch error $it")
        }.onCompletion {
            Log.d(TAG, "onCreate finally")
            println("finally")
        }.collect {
            Log.d(TAG, "onCreate collect $it")
        }
    }
}
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun ArticleScreen(onClick: (url: String) -> Unit) {
//    val articleViewModel: ArticleViewModel = viewModel()
    // 拦截返回按钮
//    BackHandler(
//        onBack = {
//            Log.d(TAG, "ArticleScreen: onBack ")
//        }
//    )
    val bannerViewModel: BannerViewModel = viewModel()
    val banners by bannerViewModel.banners.observeAsState()
    val state by bannerViewModel.stateLiveData.observeAsState()
    LoadingPage(state = state!!, loadInit = {
        bannerViewModel.getBanners()
    }) {
        Column(Modifier.fillMaxHeight()) {
//            DropdownDemo()

            banners?.apply {
                NewsBanner(this)
            }
            ArticleListPaging(onClick)
        }
    }

}

@Composable
private fun ArticleList(
    result: ResultData,
    onClick: (url: String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        val datas = result.data.datas
        items(datas, key = { data ->
            data.id
        }) { data ->
            ArticleItem2(data) {
                onClick(it)
            }
        }
//        item {
//            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator(color = Color.Red).also {
//                    Log.d(TAG, "loading: ")
//                }
//            }
//        }

    }
}

/*
* 支持分页的文章列表
* */
@Composable
private fun ArticleListPaging(
    onClick: (url: String) -> Unit
) {
    val articleViewModel: ArticleViewModel = viewModel()
    val collectAsLazyPagingItems = articleViewModel.articles1.collectAsLazyPagingItems()
    val state = rememberSwipeRefreshState(false)
    SwipeRefresh(state = state, onRefresh = {
        collectAsLazyPagingItems.refresh()
    }) {
        LazyColumn(contentPadding = PaddingValues(8.dp), modifier = Modifier.fillMaxHeight()) {
            items(collectAsLazyPagingItems) { data ->
                ArticleItem2(data!!) {
                    onClick(it)
                }
            }

            collectAsLazyPagingItems.apply {
                state.isRefreshing = loadState.refresh is LoadState.Loading
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(color = Color.Red).also {
                                    Log.d(TAG, "loading: ")
                                }
                            }
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillParentMaxWidth()
                            ) {
                                CircularProgressIndicator(color = Color.Red).also {
                                    Log.d(TAG, "loading: ")
                                }
                            }
                        }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = loadState.refresh as LoadState.Error
                        item {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable {
                                retry()
                            }) {
                                Text(text = e.error.message!!)
                            }
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = loadState.append as LoadState.Error
                        item {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable {
                                retry()
                            }) {
                                Text(text = e.error.message!!)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun <T:Any> LoadingItem(
    lazyPagingItems: LazyPagingItems<T>,
    lazyListScope: LazyListScope
) {
    when {
        lazyPagingItems.loadState.refresh is LoadState.Loading -> {
            lazyListScope.item {
                Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = Color.Red).also {
                        Log.d(TAG, "loading: ")
                    }
                }
            }
        }
        lazyPagingItems.loadState.append is LoadState.Loading -> {
            lazyListScope.item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    CircularProgressIndicator(color = Color.Red).also {
                        Log.d(TAG, "loading: ")
                    }
                }
            }
        }
        lazyPagingItems.loadState.refresh is LoadState.Error -> {
            val e = lazyPagingItems.loadState.refresh as LoadState.Error
            lazyListScope.item {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable {
                    lazyPagingItems.retry()
                }) {
                    Text(text = e.error.message!!)
                }
            }
        }
        lazyPagingItems.loadState.append is LoadState.Error -> {
            val e = lazyPagingItems.loadState.append as LoadState.Error
            lazyListScope.item {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable {
                    lazyPagingItems.retry()
                }) {
                    Text(text = e.error.message!!)
                }
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


@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun MainPage() {
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
        val viewModel: MainViewModel = viewModel()
        val selectedIndex by viewModel.getSelectedIndex().observeAsState(0)
        val pagerState = rememberPagerState(
            pageCount = 3,
            initialPage = selectedIndex,
            initialOffscreenLimit = 4
        )
        HorizontalPager(
            state = pagerState,
            dragEnabled = false,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> ArticleScreen {
                }
                1 -> PicturePage()
            }
        }
        BottomNavigationAlwaysShowLabelComponent(pagerState)
    }
}

@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun HomeScreen() {
    val items = listOf(
        Screen.Article,
        Screen.FriendsList,
        Screen.Login,
    )
    val navController = rememberNavController()
    Scaffold(topBar = {


    },
        content = { innerPadding ->
            Log.d(TAG, "bottomBar: $innerPadding")
            NavHost(
                navController = navController,
                startDestination = Screen.Article.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Article.route) {
                    Log.d(TAG, "ArticleScreen: ")
                    ArticleScreen {
                        navController.navigate("article_detail?url=$it") {
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
                    url?.let { detailUrl -> ArticleDetailScreen(detailUrl = detailUrl) }
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

@Composable
fun ArticleScreen(navController: NavController) {
    Button(onClick = {
        navController.navigate(DIALOG)
    }) {
        Text(text = "click")
    }
}

const val MINE = "mine"
const val NEST = "nest"
const val DIALOG = "dialog"

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {

    object Article : Screen("article", R.string.article, Icons.Filled.Article)
    object FriendsList : Screen("friendslist", R.string.friends_list, Icons.Filled.Favorite)
    object Login : Screen("login", R.string.login, Icons.Filled.AccountBox)
    object ArticleDetail :
        Screen("article_detail?url={url}", R.string.detail, Icons.Filled.AccountBox)
}

@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {
    val observeAsState by viewModel.searchResult.observeAsState()
    Column {
        SearchContent {
            viewModel.searchArticle(it)
        }
        if (observeAsState != null) {
            ArticleList(result = observeAsState!!) {
                Log.d(TAG, "SearchScreen: $it")
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
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
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
        if (refreshing) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color.Gray)
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

@Composable
fun ArticleItem2(data: Article, onClick: (url: String) -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    onClick(data.link)
                }
                .padding(15.dp)

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

}

@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun CardViewPreview() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray)
            .wrapContentHeight()
            .padding(top = 8.dp),
        elevation = 8.dp
    ) {
        Text(
            text = "This is a Card",
            modifier = Modifier.height(90.dp)
        )
    }
}