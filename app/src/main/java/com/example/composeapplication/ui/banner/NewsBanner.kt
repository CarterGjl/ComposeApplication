package com.example.composeapplication.ui.banner

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.composeapplication.bean.Banner
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun NewsBanner(topStories: List<Banner>) {
    val context = LocalContext.current
    Box(modifier = Modifier.height(200.dp)) {
        val pagerState = rememberPagerState(
            pageCount = topStories.size,
            infiniteLoop = true
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = rememberImagePainter(data = topStories[page].image, builder = {
                    crossfade(true)
                }),
                null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
//                        NewsDetailActivity.go(context, topStories[page].title, topStories[page].url)
                    }
            )
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(6.dp)
                .align(Alignment.BottomCenter),
            activeColor = MaterialTheme.colors.primary,
            inactiveColor = Color.White
        )
    }
}