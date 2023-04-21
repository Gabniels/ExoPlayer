package com.gabniel.exoplayer.exoplayer

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import com.gabniel.exoplayer.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView.GONE
import com.google.android.exoplayer2.ui.StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING

class ExoVideoPlayer(
    var context: Context,
    var exoVideoLayout: StyledPlayerView,
    var activity: Activity,
) {

    private var indexVideo = 0
    private var exoPlayer: ExoPlayer? = null

    //    private var position: Long = 0
    private var position: Long = 0
    private var btnPrevious: ImageView? = null
    private var btnNext: ImageView? = null
    private var btnFullScreen: ImageView? = null
    private var btnMinimize: ImageView? = null
    private var btnPlay: ImageView? = null
    private var btnPause: ImageView? = null
    private var fullScreen = true

    init {
        btnPlay = exoVideoLayout.findViewById(R.id.btnPlay)
        btnPause = exoVideoLayout.findViewById(R.id.btnPause)
        btnPrevious = exoVideoLayout.findViewById(R.id.btnPrevious)
        btnNext = exoVideoLayout.findViewById(R.id.btnNext)
    }

    fun initializationExoPlayer(data: ArrayList<String>?) {
        if (!data.isNullOrEmpty()) {
            if (indexVideo >= data.size) {
                indexVideo = 0
            }
            Log.d(TAG, "ALL DATA : $data")
            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer?.apply {
                val uri = Uri.parse(data[indexVideo])
                Log.d(TAG, "URI: $uri")
                val mediaItem = MediaItem.fromUri(uri)
                addMediaItem(mediaItem)
                moveMediaItem(indexVideo, indexVideo++)
                removeMediaItem(indexVideo--)
                prepare()
                play()
            }
            exoPlayer?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            btnPlay?.visibility = GONE
                        }
                        Player.STATE_ENDED -> {
                            destroyExo()
                            indexVideo++
                            if (indexVideo >= data.size) {
                                indexVideo = 0
                            }
                            initializationExoPlayer(data)
                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Log.d(TAG, "onPlayerError: $error")
                }
            })
            exoVideoLayout.apply {
                player = exoPlayer
                useController = true
                setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING)
            }
            onClickListener(data)
        }
    }

    private fun onClickListener(data: ArrayList<String>) {
        btnPrevious?.setOnClickListener {
            previousVideo(data)
        }

        btnPause?.setOnClickListener {
            pauseVideo()
        }
        btnPlay?.setOnClickListener {
            playVideo()
        }

        btnNext?.setOnClickListener {
            nextVideo(data)
        }
    }

    private fun pauseVideo() {
        btnPause?.visibility = GONE
        btnPlay?.visibility = VISIBLE
        exoPlayer?.pause()
    }

    private fun playVideo() {
        btnPause?.visibility = VISIBLE
        btnPlay?.visibility = GONE
        exoPlayer?.play()
    }

    private fun previousVideo(data: ArrayList<String>) {
        destroyExo()
        indexVideo--
        if (indexVideo != 0) {
            indexVideo = data.size - 1
        }
        initializationExoPlayer(data)
    }

    private fun nextVideo(data: ArrayList<String>) {
        destroyExo()
        indexVideo++
        if (indexVideo >= data.size) {
            indexVideo = 0
        }
        initializationExoPlayer(data)
    }


    fun pauseExo() {
        destroyExo()
        if (exoPlayer != null) {
            position = exoPlayer!!.currentPosition
            btnPlay?.visibility = View.VISIBLE
        }
    }

    fun positionExo() {
        if (exoPlayer != null) {
            btnPause?.visibility = View.VISIBLE
            exoPlayer?.seekTo(position)
            Log.d("TAG-ROTATE", "positionExo: $position")
            exoPlayer?.playbackState
            exoPlayer?.playWhenReady = true
        }
    }

    fun destroyExo() {
        if (exoPlayer != null) {
            exoPlayer?.stop()
            exoPlayer?.release()
        }
    }


    companion object {
        val TAG = ExoVideoPlayer::class.java.simpleName
    }

}