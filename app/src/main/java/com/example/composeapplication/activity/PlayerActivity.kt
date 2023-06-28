package com.example.composeapplication.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.composeapplication.R
import com.example.composeapplication.service.MusicPlayerService
import com.example.composeapplication.util.MetadataReaderUtils
import com.example.composeapplication.util.MusicData
import com.google.common.util.concurrent.MoreExecutors

class PlayerActivity : AppCompatActivity() {
    lateinit var player: Player
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val musicDataList = MetadataReaderUtils.getMusicDataList(this)


        setContent {
            Scaffold { it ->
                val list = remember {
                    mutableStateListOf<MusicData>()
                }
                val context = LocalContext.current
                LaunchedEffect(key1 = 1) {
                    val sessionToken =
                        SessionToken(
                            applicationContext,
                            ComponentName(context, MusicPlayerService::class.java)
                        )

                    val mediaController =
                        MediaController.Builder(context, sessionToken).buildAsync()
                    mediaController.addListener({
                        player = mediaController.get()
                        musicDataList.forEach {
                            addMediaItem(it.uri)
                            list.add(it)
                        }
                    }, MoreExecutors.directExecutor())
                }
                LazyColumn(Modifier.padding(it)) {

                    items(count = musicDataList.size) { position ->
                        Box(modifier = Modifier.clickable {
                            player.seekTo(position, 0)
                            player.prepare()
                            player.play()
                        }) {
//                            Image(painter =  BitmapPainter(musicDataList[position].album!), contentDescription = "")
                            Text(text = musicDataList[position].singer ?: "")
                        }
                    }
                }
            }
        }
    }

    fun addMediaItem(uri: Uri) {
        val newItem = MediaItem.Builder()
            .setMediaId("$uri")
            .setUri(uri)
            .build()
        player.addMediaItem(newItem)
    }

}