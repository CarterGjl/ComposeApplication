package com.example.composeapplication.ui.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.R
import com.example.composeapplication.ui.bottom.BottomNavigationAlwaysShowLabelComponent
import com.example.composeapplication.ui.weather.WeatherPage
import com.example.composeapplication.viewmodel.MainViewModel
import com.example.composeapplication.viewmodel.PictureViewModel
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun MainPage(viewModel: MainViewModel = viewModel(), pictureViewModel: PictureViewModel = viewModel(),) {
    val current = LocalContext.current
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
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onPrimary
                )
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            elevation = 0.dp
        )
        val selectedIndex by viewModel.getSelectedIndex().observeAsState(0)
        val pagerState = rememberPagerState(
            pageCount = 4,
            initialPage = selectedIndex,
            initialOffscreenLimit = 4
        )
        HorizontalPager(
            state = pagerState,
            dragEnabled = false,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> ArticleScreen { url, title ->
                    viewModel.navigateToWebViewPage(title = title, url = url)
//                    WebViewActivity.go(current as Activity, url, title = title)
                }
                1 -> PicturePage() {
                    viewModel.navigateToPhotoPage(url = it)
                }
                2 -> WeatherPage()
                3 -> FeatureThatRequiresCameraPermission(navigateToSettingsScreen = {

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", current.packageName, null)
                    }

                    current.startActivity(intent)
                })
            }
        }
        BottomNavigationAlwaysShowLabelComponent(pagerState)
    }
}