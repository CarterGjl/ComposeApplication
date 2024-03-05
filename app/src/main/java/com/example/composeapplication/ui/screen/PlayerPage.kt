package com.example.composeapplication.ui.screen

import android.Manifest
import android.content.ComponentName
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.composeapplication.R
import com.example.composeapplication.service.MusicPlayerService
import com.example.composeapplication.ui.screen.widget.MyAppBar
import com.example.composeapplication.util.MetadataReaderUtils
import com.example.composeapplication.util.MusicData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.common.util.concurrent.MoreExecutors

/**
 *Time 2023/6/28
 *Author gaojinliang
 *Describtion:
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PlayerPage() {
    val current = LocalContext.current
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    PermissionRequest(permission = permission, navigateToSettingsScreen = {
//        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
//        intent.putExtra("extra_pkgname",current.packageName)
//        val componentName = ComponentName("com.miui.securitycenter","com.miui.permcenter.permissions.PermissionsEditorActivity")
//            intent.component = componentName
        val intent =
            IntentCompat.createManageUnusedAppRestrictionsIntent(current, current.packageName)
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//            data = Uri.fromParts("package", current.packageName, null)
//        }
        current.startActivity(intent)
    }) {
        lateinit var player: Player

        Scaffold(topBar = { MyAppBar(id = R.string.music) }) { it ->


            val list = remember {
                mutableStateListOf<MusicData>()
            }
            val context = LocalContext.current
            LaunchedEffect(key1 = 1) {
                val musicDataList = MetadataReaderUtils.getMusicDataList(context)
                val sessionToken = SessionToken(
                    context.applicationContext,
                    ComponentName(context, MusicPlayerService::class.java)
                )

                val mediaController = MediaController.Builder(context, sessionToken).buildAsync()
                mediaController.addListener({
                    player = mediaController.get()
                    musicDataList.forEach {
                        val newItem =
                            MediaItem.Builder().setMediaId("${it.uri}").setUri(it.uri).build()
                        player.addMediaItem(newItem)
                        list.add(it)
                    }
                }, MoreExecutors.directExecutor())
            }
            LazyColumn(
                Modifier
                    .padding(it)
                    .padding(top = 8.dp),

                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {


                items(count = list.size) { position ->
                    val bottom = if (position == list.size - 1) {
                        WindowInsets.navigationBars.only(WindowInsetsSides.Bottom).asPaddingValues()
                            .calculateBottomPadding()
                    } else {
                        0.dp
                    }
                    val musicData = list[position]
                    MusicItem(bottom, musicData, onClick = {
                        player.seekTo(position, 0)
                        player.prepare()
                        player.play()
                    })
                }
            }
        }
    }


}

@Composable
private fun MusicItem(
    bottom: Dp,
    musicData: MusicData,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 10.dp,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .padding(bottom = bottom)
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
            ),
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                val album = musicData.album
                if (album != null) {
                    Image(
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp)
                            .clip(RoundedCornerShape(10.dp)),

                        painter = BitmapPainter(album.asImageBitmap()),
                        contentDescription = ""
                    )
                }
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = musicData.title ?: "",
                    style = TextStyle(
                        color = Color.White
                    )
                )
            }
        }
    }
}