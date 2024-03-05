package com.example.composeapplication.ui.screen

import android.os.Environment
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

private const val cacheSize: Long = 90 * 1024 * 1024 * 10
@UnstableApi
var simpleCache:SimpleCache? = null
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoScreen() {
    val context = LocalContext.current
    val exoPlayer = remember {
        val mHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
        simpleCache = simpleCache ?: SimpleCache(
            context.getExternalFilesDir(Environment.DIRECTORY_DCIM) ?:context.filesDir,
            LeastRecentlyUsedCacheEvictor(cacheSize),
            StandaloneDatabaseProvider(context)
        )
        val mCacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache!!)
            .setUpstreamDataSourceFactory(mHttpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(mCacheDataSourceFactory))
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri("https://firebasestorage.googleapis.com/v0/b/jetcalories.appspot.com/o/FROM%20EARTH%20TO%20SPACE%20_%20Free%20HD%20VIDEO%20-%20NO%20COPYRIGHT.mp4?alt=media&token=68bce5a0-7ca7-4538-9fea-98d0dc2d3646"))
                prepare()
                play()
            }
    }

    var isPlaying by remember {
        mutableStateOf(false)
    }
    var misLoading by remember {
        mutableStateOf(false)
    }

    exoPlayer.addListener(
        object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingValue: Boolean) {
                isPlaying = isPlayingValue
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                misLoading = isLoading
            }
        }
    )

    Box(contentAlignment = Alignment.Center) {
        AndroidView({
            PlayerView(context).apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                player = exoPlayer
                useController = true
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        })
        VideoLayout(isPlaying, {
            exoPlayer.play()
        }, {
            exoPlayer.pause()
        })
        if (misLoading) {
            CircularProgressIndicator()
        }

    }
    DisposableEffect(
        exoPlayer
    ) {
        onDispose { exoPlayer.release() }
    }
}

@Composable
fun VideoLayout(
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit
) {

}