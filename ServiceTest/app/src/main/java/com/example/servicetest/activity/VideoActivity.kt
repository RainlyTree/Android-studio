package com.example.servicetest.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.example.servicetest.R
import com.example.servicetest.activity.baseActivity.BaseActivity
import com.example.servicetest.databinding.ActivityVideoAcitivityBinding

class VideoActivity : BaseActivity<ActivityVideoAcitivityBinding>(), View.OnClickListener {

    override fun initViewBinding(): ActivityVideoAcitivityBinding {
        return ActivityVideoAcitivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val uri = Uri.parse("android.resource://$packageName/${R.raw.text1}")
//        binding.videoView.setVideoURI(uri)
        initVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.suspend()
    }

    fun initVideo() {
        binding.replay.setOnClickListener(this)
        binding.play.setOnClickListener(this)
        binding.pause.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.replay -> {
                if (binding.videoView.isPlaying) {
                    binding.videoView.resume()
                }
            }
            binding.play -> {
                binding.videoView.start()
            }
            binding.pause -> {
                binding.videoView.pause()
            }
        }
    }
}