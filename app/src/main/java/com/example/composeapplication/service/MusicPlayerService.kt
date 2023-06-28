package com.example.composeapplication.service

import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.example.composeapplication.R
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

/**
 *Time 2023/6/28
 *Author gaojinliang
 *Describtion:
 */
class MusicPlayerService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private lateinit var session: MediaLibrarySession
    private val PLAYBACK_NOTIFICATION_ID = 1
    private val PLAYBACK_CHANNEL_ID = "playback_channel"
    private lateinit var playerNotificationManager: PlayerNotificationManager

    @androidx.media3.common.util.UnstableApi
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(applicationContext)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setHandleAudioBecomingNoisy(true)
            .setRenderersFactory(
                DefaultRenderersFactory(this).setExtensionRendererMode(
                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                )
            ).build()
        session = MediaLibrarySession.Builder(this, player, object : MediaLibrarySession.Callback {
            override fun onAddMediaItems(
                mediaSession: MediaSession,
                controller: MediaSession.ControllerInfo,
                mediaItems: MutableList<MediaItem>
            ): ListenableFuture<MutableList<MediaItem>> {
                val updatedMediaItems =
                    mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
                return Futures.immediateFuture(updatedMediaItems)
            }
        }).build()

        initNotification()
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun initNotification() {
        playerNotificationManager = PlayerNotificationManager.Builder(
            applicationContext,
            PLAYBACK_NOTIFICATION_ID,
            PLAYBACK_CHANNEL_ID
        )
            .setSmallIconResourceId(R.drawable.ic_bruce)
            .build()
//        playerNotificationManager.setColorized(true)
//        playerNotificationManager.setColor(0xFF2d3b2d.toInt())
        playerNotificationManager.setPlayer(player)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession {
        return session
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onDestroy() {
        super.onDestroy()
        session.release()
        playerNotificationManager.setPlayer(null)
        player.release()

    }
}