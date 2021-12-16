package com.example.composeapplication.ui.screen

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.DIALOG
import com.example.composeapplication.bean.Article
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.ui.banner.NewsBanner
import com.example.composeapplication.viewmodel.ArticleViewModel
import com.example.composeapplication.viewmodel.BannerViewModel
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


private const val TAG = "ArticleScreen"

@Composable
fun ArticleList(
    result: ResultData,
    onClick: (url: String, title: String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        val datas = result.data.datas
        items(datas, key = { data ->
            data.id
        }) { data ->
            ArticleItem2(data) {
                onClick(it, data.title)
            }
        }
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Red).also {
                    Log.d(TAG, "loading: ")
                }
            }
        }

    }
}

/*
* 支持分页的文章列表
* */
@Composable
private fun ArticleListPaging(
    onClick: (url: String, title: String) -> Unit
) {
    val articleViewModel: ArticleViewModel = viewModel()
    val viewState = remember {
        articleViewModel.viewStates
    }
    val collectAsLazyPagingItems = viewState.pagingData.collectAsLazyPagingItems()
    val state = rememberSwipeRefreshState(false)
    val listState = if (collectAsLazyPagingItems.itemCount > 0) viewState.listState else LazyListState()
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxHeight(),
    ) {
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
fun ArticleScreen(onClick: (url: String, title: String) -> Unit) {
    val bannerViewModel: BannerViewModel = viewModel()
    val banners by bannerViewModel.banners.observeAsState()
    val state by bannerViewModel.stateLiveData.observeAsState()
    Column(Modifier.fillMaxHeight()) {
        banners?.apply {
            NewsBanner(this)
        }
        ArticleListPaging(onClick)
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

@Composable
fun ArticleDetailScreen(detailUrl: String, title: String = "",naviBack:()->Unit = {}) {
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
                    text = title,
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
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            refreshing = false
                        }
                    }
                }
            },
            update = { view ->
                // view 被 inflated
                view.loadUrl(detailUrl)
                webView = view
            })

        BackHandler {
            if (webView?.canGoBack() == true){
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


