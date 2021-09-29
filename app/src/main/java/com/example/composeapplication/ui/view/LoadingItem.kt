package com.example.composeapplication.ui.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems


private const val TAG = "LoadingItem"

@Composable
private fun <T : Any> LoadingItem(
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable {
                        lazyPagingItems.retry()
                    }
                ) {
                    Text(text = e.error.message!!)
                }
            }
        }
        lazyPagingItems.loadState.append is LoadState.Error -> {
            val e = lazyPagingItems.loadState.append as LoadState.Error
            lazyListScope.item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable {
                        lazyPagingItems.retry()
                    }
                ) {
                    Text(text = e.error.message!!)
                }
            }
        }
    }
}