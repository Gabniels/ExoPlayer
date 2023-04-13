package com.gabniel.exoplayer

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gabniel.exoplayer.databinding.ActivityMainBinding
import com.gabniel.exoplayer.exoplayer.ExoVideoPlayer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var exoVideoPlayer: ExoVideoPlayer
    private var list: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        initializationExo()
        getData()
    }

    override fun onPause() {
        super.onPause()
        exoVideoPlayer.pauseExo()
    }

    override fun onResume() {
        super.onResume()
        exoVideoPlayer.destroyExo()
        exoVideoPlayer.initializationExoPlayer(list)
        exoVideoPlayer.positionExo()
    }

    private fun initializationExo() {
        exoVideoPlayer = ExoVideoPlayer(applicationContext, binding.exoVideo)
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