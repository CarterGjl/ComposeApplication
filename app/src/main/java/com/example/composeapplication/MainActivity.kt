@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.composeapplication

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarms
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.core.view.postDelayed
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.activity.bsae.BaseActivity
import com.example.composeapplication.ui.screen.SplashAdScreen
import com.example.composeapplication.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.material.navigation.NavigationView


// 官方demo地址
// https://github.com/android/compose-samples

@Suppress("EXPERIMENTAL_ANNOTATION_ON_OVERRIDE_WARNING")
class MainActivity : BaseActivity(), SplashScreen.OnExitAnimationListener {

    private val mainViewModel: MainViewModel by viewModels()

    @ExperimentalCoilApi
    @ExperimentalPagerApi
    @ExperimentalFoundationApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_main)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        splashScreen.setKeepVisibleCondition {
            false
        }

        splashScreen.setOnExitAnimationListener(this)
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun onSplashScreenExit(splashScreenViewProvider: SplashScreenViewProvider) {

        if (splashScreenViewProvider.view is ViewGroup) {
            //显示一个广告或者启动页推广,自己实践玩耍吧,建议把mainViewModel.mockDataLoading()延时降低，然后测试
            val composeView = ComposeView(this@MainActivity).apply {
                setContent {
                    SplashAdScreen(onCloseAd = {
                        splashScreenViewProvider.remove()
                    })
                }
            }
            (splashScreenViewProvider.view as ViewGroup).addView(composeView)
            return
        }

        val flag = false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || flag) {
            // 使用alpha透明度动画过渡
            val splashScreenView = splashScreenViewProvider.view
            val endAlpha = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) 0F else -2F
            val alphaObjectAnimator =
                ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1F, endAlpha)
            alphaObjectAnimator.duration = 500L
            alphaObjectAnimator.interpolator = FastOutLinearInInterpolator()
            alphaObjectAnimator.doOnEnd {
                splashScreenViewProvider.remove()
            }
            alphaObjectAnimator.start()
            return
        }

        //下面是所有使用动态背景的，我们让中心图标做一个动画然后离开
        val splashScreenView = splashScreenViewProvider.view
        val iconView = splashScreenViewProvider.iconView
        val isCompatVersion = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
        val slideUp = ObjectAnimator.ofFloat(
            iconView,
            View.TRANSLATION_Y,
            0f,
            -splashScreenView.height.toFloat()
        )
        slideUp.interpolator = AnticipateInterpolator()
        slideUp.duration = if (isCompatVersion) 1000L else 200L
        slideUp.doOnEnd {
            splashScreenViewProvider.remove()
        }
        if (isCompatVersion) {
            //低版本的系统，我们让图标做完动画再关闭
            waitForAnimatedIconToFinish(splashScreenViewProvider, splashScreenView, slideUp)
        } else {
            slideUp.start()
        }
    }

    private fun waitForAnimatedIconToFinish(
        splashScreenViewProvider: SplashScreenViewProvider,
        view: View,
        animator: Animator
    ) {
        val delayMillis: Long = (
                splashScreenViewProvider.iconAnimationStartMillis +
                        splashScreenViewProvider.iconAnimationDurationMillis
                ) - System.currentTimeMillis()
        view.postDelayed(delayMillis) { animator.start() }
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


const val MINE = "mine"
const val NEST = "nest"
const val DIALOG = "dialog"

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {

    object Article : Screen("article", R.string.article, Icons.Filled.Article)
    object Picture : Screen("picture", R.string.picture, Icons.Filled.Favorite)
    object Weather : Screen("weather", R.string.weather, Icons.Filled.AccountBox)
    object Test : Screen("test", R.string.test, Icons.Filled.AccessAlarms)
    object ArticleDetail :
        Screen("article_detail?url={url}", R.string.detail, Icons.Filled.AccountBox)
}
