package com.example.composeapplication.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Build
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
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
import com.example.composeapplication.widget.FpsMonitor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.gson.Gson


private const val TAG = "ArticleScreen"

@OptIn(
    ExperimentalFoundationApi::class,
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

//        item {
//            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw
//                .loading_moving_box))
//            val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
//            LottieAnimation(
//                composition = composition,
//                progress = { progress },
//            )
//        }
        stickyHeader {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                ListItem(headlineContent = {
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
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                ListItem(headlineContent = {
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
                            style = MaterialTheme.typography.bodySmall,
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
@OptIn(ExperimentalMaterial3Api::class)
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
    var refreshing by remember { mutableStateOf(false) }
    Log.d(TAG, "ArticleListPaging: $refreshing")

    val pullRefreshState =
        rememberPullToRefreshState(enabled = { true })
    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            // fetch something
            collectAsLazyPagingItems.refresh()
        }
    }
    val listState =
        if (collectAsLazyPagingItems.itemCount > 0) viewState.listState else LazyListState()
    Box(Modifier.nestedScroll(pullRefreshState.nestedScrollConnection)) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.fillMaxHeight(),
        ) {

            item {
                banners?.let { NewsBanner(it, onClick) }
            }
            items(
                count = collectAsLazyPagingItems.itemCount,
                key = collectAsLazyPagingItems.itemKey(),
                contentType = collectAsLazyPagingItems.itemContentType(
                )
            ) { index ->
                val item = collectAsLazyPagingItems[index]
                ArticleItem2(item!!) {
                    onClick(it, item.title)
                }
            }

            collectAsLazyPagingItems.apply {
                refreshing = loadState.refresh is LoadState.Loading
                if (!refreshing) {
                    pullRefreshState.endRefresh()
                }
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
        val scaleFraction = if (pullRefreshState.isRefreshing) 1f else
            LinearOutSlowInEasing.transform(pullRefreshState.progress).coerceIn(0f, 1f)

        Box(modifier = Modifier
            .padding(top = 60.dp)
            .align(Alignment.TopCenter)) {
            PullToRefreshContainer(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction),
                state = pullRefreshState,
            )
        }

    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ArticleScreen(
    navController: NavController,
    onClick: (url: String, title: String) -> Unit
) {

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        //When the user has selected a photo, its URL is returned here
    }
    val strings = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    val rememberMultiplePermissionsState = rememberMultiplePermissionsState(permissions = strings.asList())

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
                    IconButton(onClick = {
                        launcher.launch(
                            PickVisualMediaRequest(
                            //Here we request only photos. Change this to .ImageAndVideo if
                            //you want videos too.
                            //Or use .VideoOnly if you only want videos.
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                        )

                    }) {
                        Icon(Icons.Filled.Photo, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FpsMonitor(modifier = Modifier.padding(bottom = 80.dp))
        }
    ) {
        LoadingPage(state = state, loadInit = {
            bannerViewModel.getBanners()
        }) {
            Column(
                Modifier
                    .padding(it)
                    .fillMaxHeight()
            ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleDetailScreen(
    detailUrl: String, title: String = "",
    darkTheme: Boolean = isSystemInDarkTheme(),
    naviBack: () -> Unit = {}
) {
    Log.d(TAG, "ArticleDetailScreen: ")
    var refreshing by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .statusBarsPadding()
    ) {
        TopAppBar(
            title = {
                SelectionContainer {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = title.parseHighlight(),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    naviBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            isAlgorithmicDarkeningAllowed = darkTheme
                        } /*else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val webViewTheme = if (darkTheme) {
                                WebSettings.FORCE_DARK_ON
                            } else {
                                WebSettings.FORCE_DARK_OFF
                            }
                            forceDark = webViewTheme
                        }*/
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
            style = MaterialTheme.typography.titleMedium
        )
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Icon(imageVector = Icons.Filled.Timeline, contentDescription = "time")
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = data.niceDate,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
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
        modifier = Modifier
            .fillMaxWidth(),
        shadowElevation = 10.dp
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
                style = MaterialTheme.typography.titleMedium
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Icon(imageVector = Icons.Filled.Update, contentDescription = "time")
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = data.niceDate,
                    style = MaterialTheme.typography.bodyLarge
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
                    style = MaterialTheme.typography.bodyLarge,
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
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HotkeyItem(
    hotkeys: List<HotKey>,
    onSelected: (key: String) -> Unit
) {
    FlowRow(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
    text: String,
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
                color = if (isSelect && !isLoading) Color.Black else MaterialTheme.colorScheme.primaryContainer,
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



