package com.example.composeapplication.ui.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.CircleCropTransformation
import com.example.composeapplication.R
import com.example.composeapplication.ui.screen.widget.MyAppBar
import com.example.composeapplication.viewmodel.PictureViewModel

private const val TAG = "PicturePage"

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun PicturePage(
    viewModel: PictureViewModel = viewModel(),
    navigateToPhotoPage: (url: String) -> Unit = {}
) {
    val picList = viewModel.pics.collectAsLazyPagingItems()
    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            MyAppBar(id = R.string.picture)
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()) {
           Box {
               LazyVerticalGrid(
                   columns = GridCells.Fixed(3),
                   contentPadding = PaddingValues(5.dp),
                   modifier = Modifier
                       .align(Alignment.Center)
                       .fillMaxWidth()
               ) {
                   items(picList.itemCount) { index ->
                       val item = picList[index]
                       Image(
                           painter = rememberAsyncImagePainter(
                               ImageRequest.Builder(LocalContext.current)
                                   .data(item!!.url)
                                   .size(Size.ORIGINAL)
                                   .crossfade(true)
                                   .placeholder(R.drawable.ic_loading)
                                   .transformations(CircleCropTransformation())
                                   .build()
                           ),
                           null,
                           contentScale = ContentScale.Inside,
                           modifier = Modifier
                               .padding(5.dp)
                               .width(120.dp)
                               .height(120.dp)
                               .clickable {
                                   navigateToPhotoPage(item.url)
                               }
                       )
                   }
                   picList.apply {
                       refreshing = loadState.refresh is LoadState.Loading
                       when {
                           loadState.refresh is LoadState.Loading -> {
                               item {
                                   Box(
                                       modifier = Modifier.fillMaxWidth(),
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
                                       modifier = Modifier.fillMaxWidth()
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
                                       modifier = Modifier
                                           .fillMaxWidth()
                                           .clickable {
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
                                       modifier = Modifier
                                           .fillMaxWidth()
                                           .clickable {
                                               retry()
                                           }) {
                                       Text(text = e.error.message!!)
                                   }
                               }
                           }
                       }
                   }
                   refreshing = picList.loadState.refresh is LoadState.Loading
               }
               PullToRefreshContainer(
                   modifier = Modifier
                       .align(Alignment.TopCenter),
//                       .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction),
                   state = pullRefreshState,
               )

           }
        }
    }

}
