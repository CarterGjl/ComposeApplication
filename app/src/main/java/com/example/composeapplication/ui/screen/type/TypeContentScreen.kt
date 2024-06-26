package com.example.composeapplication.ui.screen.type

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeapplication.Screen
import com.example.composeapplication.extend.LocalNavHostController
import com.example.composeapplication.ui.screen.ArticleItem2
import com.example.composeapplication.ui.screen.type.bean.TreeListResponse
import com.example.composeapplication.ui.screen.type.viewmodel.TypeContentViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder

private const val TAG = "TypeContentScreen"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TypeContentScreen(knowledge: TreeListResponse.Knowledge) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        knowledge.children?.size ?: 0
    }
    Scaffold(
        topBar = {
            TypeContentAppbar(knowledge, pagerState)
        },
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) { page ->
            val banner = knowledge.children?.get(page)
            Column(
                Modifier.fillMaxSize(),
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0x80000000))
                ) {
                    Log.d(TAG, "TypeContentScreen: $page")
                    if (banner != null) {
                        TypeContentDetail(pagerState, knowledge)
                    }
                }
            }

        }
    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun TypeContentAppbar(
    knowledge: TreeListResponse.Knowledge,
    pagerState: PagerState
) {
    val navHostController = LocalNavHostController.current
    val tabDatas = knowledge.children
    val tabIndex = pagerState.currentPage
    Column {
        TopAppBar(
            title = {
                Text(
                    text = knowledge.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        )
        ScrollableTabRow(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedTabIndex = tabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentColor = MaterialTheme.colorScheme.secondary,
            edgePadding = 0.dp,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(it[tabIndex]),
                    color = Color.White,
                    height = 4.dp
                )
            },
            tabs = {
                tabDatas?.forEachIndexed { index, s ->
                    LeadingIconTabView(index, s, pagerState)
                }
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LeadingIconTabView(
    index: Int,
    text: TreeListResponse.Knowledge.Children,
    pagerState: PagerState,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val rememberCoroutineScope = rememberCoroutineScope()
    val tabIndex = pagerState.currentPage
    val isPress = interactionSource.collectIsPressedAsState().value
    Tab(
        selected = index == tabIndex,
        onClick = {
            rememberCoroutineScope.launch {
                pagerState.scrollToPage(index)

            }
        },
        text = {
            Text(
                text = text.name,
                color = if (isPress || index == tabIndex) {
                    Color.White
                } else Color.Gray
            )
        },
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight(),
        enabled = true,
        interactionSource = interactionSource,
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TypeContentDetail(
    pagerState: PagerState,
    knowledge: TreeListResponse.Knowledge,
    typeContentViewModel: TypeContentViewModel = viewModel()
) {
    val navHostController = LocalNavHostController.current
    val list by typeContentViewModel.articalLiveData.observeAsState(initial = emptyList())
    val currentPage = pagerState.currentPage
    val id = knowledge.children?.get(currentPage)?.id ?: 0
    LaunchedEffect(id) {
        typeContentViewModel.getArticleList(0, id)
    }

    LazyColumn(
        contentPadding = PaddingValues(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxHeight(),
    ) {
        items(list) { data ->
            ArticleItem2(data = data, onClick = {
                val encode = URLEncoder.encode(it, "utf-8")
                navHostController.navigate(Screen.WebView.route + "?title=${data.title}&url=$encode") {
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            })
        }
    }
}