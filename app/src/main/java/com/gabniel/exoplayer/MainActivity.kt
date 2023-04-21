package com.gabniel.exoplayer

import android.R
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gabniel.exoplayer.databinding.ActivityMainBinding
import com.gabniel.exoplayer.exoplayer.ExoVideoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var exoVideoPlayer: ExoVideoPlayer
    private var list: ArrayList<String>? = null


    private var mFullScreenDialog: Dialog? = null
    private var mExoPlayerFullscreen = false
    private val mExoPlayerView: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initializationExo()
        getData()
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG-LIFECYCLE", "onPause: ")
        exoVideoPlayer.pauseExo()
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG-LIFECYCLE", "onResume: ")
        exoVideoPlayer.destroyExo()
        exoVideoPlayer.initializationExoPlayer(list)
        exoVideoPlayer.positionExo()
    }

    private fun initializationExo() {
        exoVideoPlayer = ExoVideoPlayer(applicationContext, binding.exoVideo, this)
    }

    private fun getData() {
        val videoOne =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        val videoTwo =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"

        list = arrayListOf(videoOne, videoTwo)

        exoVideoPlayer.initializationExoPlayer(list)
    }

}