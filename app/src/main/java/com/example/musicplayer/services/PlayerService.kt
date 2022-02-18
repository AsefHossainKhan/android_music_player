package com.example.musicplayer.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.Nullable
import com.example.musicplayer.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import timber.log.Timber

class PlayerService : Service() {
    private var myBinder = MyBinder()
    var player: ExoPlayer? = null
    var mediaSession: MediaSessionCompat? = null
    var mediaSessionConnector: MediaSessionConnector? = null
    var playerNotificationManager: PlayerNotificationManager? = null
    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
//        return null
    }

    inner class MyBinder : Binder() {
        fun currentService(): PlayerService {
            return this@PlayerService
        }
    }

    fun playPausePlayer() {
        if (player!!.isPlaying)
            player!!.pause()
        else
            player!!.play()
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(applicationContext).build()
        Timber.e("End")
        // Build the media item.
        val url = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_1MG.mp3"
        val url2 =
            "https://multiplatform-f.akamaihd.net/i/multi/will/bunny/big_buck_bunny_,640x360_400,640x360_700,640x360_1000,950x540_1500,.f4v.csmil/master.m3u8"
//        val mediaItem = MediaItem.fromUri(url)
//        // Set the media item to be played.
//        player!!.setMediaItem(mediaItem)
        player!!.addMediaItem(MediaItem.fromUri(url))
        player!!.addMediaItem(MediaItem.fromUri(url2))
        // Prepare the player.
        player!!.prepare()
        // Start the playback.
        Timber.e("Play porjonto ashche")
        player!!.play()

        playerNotificationManager = PlayerNotificationManager.Builder(
            baseContext,
            1, "CHANNEL_ID"
        )
            .setChannelNameResourceId(R.string.channel_name)
            .setChannelDescriptionResourceId(R.string.channel_description)
            .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return "THIS IS THE SONG TITLE"
                }

                @Nullable
                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return null
                }

                @Nullable
                override fun getCurrentContentText(player: Player): String? {
                    return "SONG ER TEXT"
                }

                @Nullable
                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return BitmapFactory.decodeResource(resources, R.raw.raven)
                }
            })
            .setNotificationListener(object :
                PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    super.onNotificationPosted(notificationId, notification, ongoing)

                    startForeground(notificationId, notification)
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    super.onNotificationCancelled(notificationId, dismissedByUser)
                    stopSelf()
                }
            })
            .build()
        playerNotificationManager!!.setPlayer(player)

//        mediaSession = MediaSessionCompat(baseContext, "audio_demo")
//        mediaSession!!.isActive = true
//        playerNotificationManager!!.setMediaSessionToken(mediaSession!!.sessionToken)

//        mediaSessionConnector = MediaSessionConnector(mediaSession!!)
//        mediaSessionConnector.setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
//            override fun getMediaDescription(
//                player: Player,
//                windowIndex: Int
//            ): MediaDescriptionCompat {
//                TODO()
//            }
//        })
//        mediaSessionConnector!!.setPlayer(player)
    }

    override fun onDestroy() {
        Timber.e("ON DESTROY CALLED")
        mediaSession?.release();
        mediaSessionConnector?.setPlayer(null);
        playerNotificationManager?.setPlayer(null);
        player?.release();
        player = null;
    }
}
