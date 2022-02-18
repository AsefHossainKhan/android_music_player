package com.example.musicplayer.ui.fragments

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.musicplayer.databinding.FragmentPlayerBinding
import com.example.musicplayer.services.PlayerService
import com.google.android.exoplayer2.Player
import timber.log.Timber

class PlayerFragment : Fragment(), Player.Listener, ServiceConnection {
    lateinit var binding: FragmentPlayerBinding

    companion object {
        var playerService: PlayerService? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        startService()
        initOnclicks()
        return binding.root
    }
    private fun initOnclicks() {
        binding.playButton.setOnClickListener {
            playerService?.playPausePlayer()
        }
    }

    private fun startService() {
        Timber.e("Start Service")
        val intent = Intent(activity, PlayerService::class.java)
        activity!!.bindService(intent, this, BIND_AUTO_CREATE)
        activity!!.startService(intent)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//        if (playbackState == Player.STATE_BUFFERING)
//            binding.progressBar.visibility = View.VISIBLE
//        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
//            binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Timber.e("SERVICE CONNECTED")
        val binder = service as PlayerService.MyBinder
        playerService = binder.currentService()
//        binding.mediaPlayer.player = playerService!!.player
        playerService!!.player!!.addListener(this)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Timber.e("SERVICE DISCONNECTED")
        playerService = null
    }

    override fun onDestroy() {
        playerService?.stopSelf()
        super.onDestroy()
    }
//    override fun onStart() {
//        super.onStart()
//        initializePlayer()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        releasePlayer()
//    }
//
//    private fun initializePlayer() {
//        exoPlayer = ExoPlayer.Builder(applicationContext).build()
//        val randomUrl = urlList.random()
//        preparePlayer(randomUrl.first, randomUrl.second)
//        binding.mediaPlayer.player = exoPlayer
//        exoPlayer.seekTo(playbackPosition)
//        exoPlayer.playWhenReady = true
//        exoPlayer.addListener(this)
//    }
//
//    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
//        return if (type == "dash") {
//            DashMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(uri)
//        } else {
//            ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(uri)
//        }
//    }
//
//    private fun preparePlayer(videoUrl: String, type: String) {
//        val uri = Uri.parse(videoUrl)
//        val mediaSource = buildMediaSource(uri, type)
//        exoPlayer.prepare(mediaSource)
//    }
//
//    private fun releasePlayer() {
//        playbackPosition = exoPlayer.currentPosition
//        exoPlayer.release()
//    }
//
//    fun onPlayerError(error: ExoPlaybackException) {
//        // handle error
//    }
//
}