package com.example.composeapplication.ui.screen

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.composeapplication.R
import com.example.composeapplication.activity.PhotoActivity
import com.example.composeapplication.viewmodel.PictureViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun PicturePage(viewModel: PictureViewModel = viewModel()) {
    val context = LocalContext.current
    val picList = viewModel.pics.collectAsLazyPagingItems()
    val state = rememberSwipeRefreshState(false)
    Column(Modifier.fillMaxSize()) {
        SwipeRefresh(
            state = state,
            onRefresh = {
                picList.refresh()
            }
        ) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(3),
                contentPadding = PaddingValues(5.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                items(picList) { item ->
                    Image(
                        painter = rememberImagePainter(
                            item!!.url,
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.ic_loading)
                                transformations(CircleCropTransformation())
                            }),
                        null,
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .padding(5.dp)
                            .width(120.dp)
                            .height(120.dp)
                            .clickable {
                                PhotoActivity.go(context as Activity, item.url)
                            }
                    )
                }
                state.isRefreshing = picList.loadState.refresh is LoadState.Loading
            }
        }
    }

}
@ExperimentalFoundationApi
fun <T: Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}