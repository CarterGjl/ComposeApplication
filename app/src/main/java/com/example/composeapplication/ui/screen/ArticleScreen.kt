@file:Suppress("OPT_IN_IS_NOT_ENABLED", "DEPRECATION", "unused")

package com.example.composeapplication.ui.screen

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Build
import android.util.Log
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.DIALOG
import com.example.composeapplication.R
import com.example.composeapplication.Screen
import com.example.composeapplication.bean.Article
import com.example.composeapplication.bean.HotKey
import com.example.composeapplication.extend.LocalNavHostController
import com.example.composeapplication.extend.parseHighlight
import com.example.composeapplication.ui.banner.NewsBanner
import com.example.composeapplication.ui.screen.type.bean.TreeListResponse
import com.example.composeapplication.ui.screen.widget.MyAppBar
import com.example.composeapplication.viewmodel.ArticleViewModel
import com.example.composeapplication.viewmodel.BannerViewModel
import com.example.composeapplication.viewmodel.State
import com.example.composeapplication.viewmodel.search.SearchViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import org.jetbrains.annotations.NotNull


private const val TAG = "ArticleScreen"

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
@Composable
fun ArticleList(
    result: List<Article>,
    viewModel: SearchViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
    onClick: (url: String, title: String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val hotkeys: List<HotKey> by viewModel.hotKeyResult.observeAsState(emptyList())
    LaunchedEffect(true) {
        viewModel.getHotKeys()
    }
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        stickyHeader {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primaryVariant)
            ) {
                ListItem(text = {
                    Text(text = "搜索热词")
                })
            }

        }

        item {
            if (hotkeys.isNotEmpty()) {
                Box {
                    HotkeyItem(
                        hotkeys = hotkeys,
                        onSelected = { text ->
                            keyboardController?.hide()
                            searchViewModel.searchArticle(text)
                        })
                }
            }
        }
        stickyHeader {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primaryVariant)
            ) {
                ListItem(text = {
                    Text(text = "搜索结果")
                })
            }
        }
        if (result.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.mipmap.status_empty),
                            modifier = Modifier.size(200.dp),
                            contentDescription = ""
                        )
                        Text(
                            text = "这里空空的。。。",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
        items(result, key = { data ->
            data.id
        }) { data ->
            ArticleItem2(data) {
                onClick(it, data.title)
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
@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
private fun ArticleListPaging(
    onClick: (url: String, title: String) -> Unit
) {
    val articleViewModel: ArticleViewModel = viewModel()
    val viewState = remember {
        articleViewModel.viewStates
    }
    val bannerViewModel: BannerViewModel = viewModel()
    val banners by bannerViewModel.banners.observeAsState()

    val collectAsLazyPagingItems = viewState.pagingData.collectAsLazyPagingItems()
    val state = rememberSwipeRefreshState(false)
    val listState =
        if (collectAsLazyPagingItems.itemCount > 0) viewState.listState else LazyListState()
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxHeight(),
    ) {
        item {
            banners?.let { NewsBanner(it, onClick) }
        }
        items(collectAsLazyPagingItems) { data ->
            ArticleItem2(data!!) {
                onClick(it, data.title)
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
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.clickable {
                                retry()
                            }) {
                            Text(text = e.error.message!!)
                        }
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = loadState.append as LoadState.Error
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.clickable {
                                retry()
                            }) {
                            Text(text = e.error.message!!)
                        }
                    }
                }
            }
        }
    }
//    SwipeRefresh(state = state, onRefresh = {
//        collectAsLazyPagingItems.refresh()
//    }) {
//
//    }

}

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun ArticleScreen(
    navController: NavController,
    onClick: (url: String, title: String) -> Unit
) {
    val bannerViewModel: BannerViewModel = viewModel()
    val state by bannerViewModel.stateLiveData.observeAsState(State.Loading)
    Scaffold(
        topBar = {
            MyAppBar(
                id = R.string.article,
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Search.route)
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )
        },
    ) {
        LoadingPage(state = state, loadInit = {
            bannerViewModel.getBanners()
        }) {
            Column(
                Modifier
                    .padding(it)
                    .fillMaxHeight()) {
                ArticleListPaging(onClick)
            }
        }
    }

}

@Composable
fun ArticleScreen(navController: NavController) {
    Button(onClick = {
        navController.navigate(DIALOG)
    }) {
        Text(text = "click")
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleDetailScreen(
    detailUrl: String, title: String = "",
    darkTheme: Boolean = isSystemInDarkTheme(),
    naviBack: () -> Unit = {}
) {
    Log.d(TAG, "ArticleDetailScreen: ")
    var refreshing by remember { mutableStateOf(true) }
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
                    text = title.parseHighlight(),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    naviBack()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            elevation = 0.dp
        )
        if (refreshing) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color.Gray)
        }
        var webView: WebView? = null
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                // Sets up
                WebView(context).apply {
                    Log.d(TAG, "ArticleDetailScreen: apply")
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String) {
                            super.onPageFinished(view, url)
                            refreshing = false
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest
                        ): Boolean {
                            if (request.url.scheme?.startsWith("http") == true) {
                                return super.shouldOverrideUrlLoading(view, request)
                            }
                            return true
                        }

                        @SuppressLint("WebViewClientOnReceivedSslError")
                        override fun onReceivedSslError(
                            view: WebView?,
                            handler: SslErrorHandler?,
                            error: SslError?
                        ) {
                            super.onReceivedSslError(view, handler, error)
                            handler?.proceed()
                        }
                    }
                    loadUrl(detailUrl)
                }

            },
            update = { view ->
                view.apply {


                    settings.apply {
                        if (Build.VERSION.SDK_INT >= 33) {
                            isAlgorithmicDarkeningAllowed = darkTheme
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val webViewTheme = if (darkTheme) {
                                WebSettings.FORCE_DARK_ON
                            } else {
                                WebSettings.FORCE_DARK_OFF
                            }
                            forceDark = webViewTheme
                        }
                        javaScriptEnabled = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        domStorageEnabled = true
                    }
                }
                // view 被 inflated
                Log.d(TAG, "ArticleDetailScreen: update")
                webView = view
            })

        BackHandler {
            if (webView?.canGoBack() == true) {
                webView?.goBack()
            } else {
                naviBack()
            }
        }
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
    val navHostController = LocalNavHostController.current
    Surface(
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
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
                text = data.title.parseHighlight(),
                style = MaterialTheme.typography.subtitle1
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Icon(imageVector = Icons.Filled.Update, contentDescription = "time")
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = data.niceDate,
                    style = MaterialTheme.typography.body1
                )
            }
//            Row(modifier = Modifier.padding(top = 8.dp)) {
//                Text(
//                    style = MaterialTheme.typography.body1,
//                    color = Color.Red,
//                    text = data.superChapterName,
//                    modifier = Modifier
//                        .border(
//                            width = 1.dp,
//                            color = Color.Red,
//                            shape = RoundedCornerShape(
//                                topStart = 8.dp,
//                                topEnd = 8.dp,
//                                bottomStart = 8.dp,
//                                bottomEnd = 8.dp
//                            )
//                        )
//                        .padding(10.dp)
//                )
//            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    style = MaterialTheme.typography.body1,
                    color = Color.Red,
                    text = data.chapterName,
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
                        .clickable {
                            val list: List<TreeListResponse.Knowledge.Children> = listOf(
                                TreeListResponse.Knowledge.Children(
                                    data.chapterId,
                                    data.chapterName, 0, 0, 0, 0, null
                                )
                            )
                            val knowledge = TreeListResponse.Knowledge(
                                0, "", 0, 0, 0, 0, list
                            )
                            val toJson = Gson().toJson(knowledge)

                            navHostController.navigate("type_content?knowledge=$toJson") {

                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                        .padding(10.dp)
                )
            }
        }
    }

}

/**
 * 搜索热词的item
 */
@Composable
fun HotkeyItem(
    hotkeys: List<HotKey>,
    onSelected: (key: String) -> Unit
) {
    FlowRow(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        mainAxisSize = SizeMode.Expand
    ) {
        hotkeys.forEach {
            LabelTextButton(
                text = it.name,
                isSelect = false,
                modifier = Modifier.padding(end = 5.dp, bottom = 5.dp),
                onClick = {
                    onSelected(it.name)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelTextButton(
    @NotNull text: String,
    modifier: Modifier = Modifier,
    isSelect: Boolean = true,
    specTextColor: Color? = null,
    cornerValue: Dp = 25.dp / 2,
    isLoading: Boolean = false,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) {
    Text(
        text = text,
        modifier = modifier
            .height(25.dp)
            .clip(shape = RoundedCornerShape(cornerValue))
            .background(
                color = if (isSelect && !isLoading) Color.Black else MaterialTheme.colors.primaryVariant,
            )
            .combinedClickable(
                enabled = !isLoading,
                onClick = { onClick?.invoke() },
                onLongClick = { onLongClick?.invoke() }
            )
            .padding(
                horizontal = 10.dp,
                vertical = 3.dp
            ),
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        color = specTextColor ?: if (isSelect) Color.White else Color.White,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}



