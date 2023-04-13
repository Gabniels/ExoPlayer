package com.gabniel.exoplayer.exoplayer

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING

class ExoVideoPlayer(
    var context: Context,
    var exoVideoLayout: StyledPlayerView,
) {

    private var indexVideo = 0
    private var exoPlayer: ExoPlayer? = null
    private var position: Long = 0

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
        }
    }

    fun pauseExo() {
        destroyExo()
        if (exoPlayer != null) {
            position = exoPlayer!!.currentPosition
        }
    }

    fun positionExo() {
        if (exoPlayer != null) {
            exoPlayer?.seekTo(position)
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