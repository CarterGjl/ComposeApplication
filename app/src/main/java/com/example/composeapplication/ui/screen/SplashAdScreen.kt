package com.example.composeapplication.ui.screen

import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.composeapplication.R
import com.example.composeapplication.activity.util.getStatusBarHeight
import com.example.composeapplication.activity.util.px2dp
import com.example.composeapplication.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@ExperimentalCoilApi
@Composable
fun SplashAdScreen(onCloseAd:()->Unit,splashViewModel: SplashViewModel = viewModel()){

    Box(modifier = Modifier.fillMaxSize()){
        //广告或者推广的背景图
        var time by remember {
            mutableStateOf(3)
        }
        LaunchedEffect(true) {
            splashViewModel.countDownCoroutines(3, onTick = {
                time = it
            }, onFinish = {
                onCloseAd()
            })
        }
        Image(
            painter = rememberImagePainter(data = R.drawable.splash, builder = {
                crossfade(true)
            }),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        //倒计时按钮，这里仅测试一下效果
        Button(onClick = {
            onCloseAd()
        },modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(
                end = 16.dp,
                top = LocalDensity.px2dp(px = LocalContext.current.resources.getStatusBarHeight())
            )) {
            Text(text = "倒计时 $time")
        }
    }
}