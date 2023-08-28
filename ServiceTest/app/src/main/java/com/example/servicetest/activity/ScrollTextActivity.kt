package com.example.servicetest.activity

import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.servicetest.activity.baseActivity.BaseActivity
import com.example.servicetest.databinding.ActivityScrollTextBinding
import com.example.servicetest.recyclerViewManager.SlidingHelper

class ScrollTextActivity : BaseActivity<ActivityScrollTextBinding>(), View.OnClickListener {

    var slidingHelper: SlidingHelper? = null

    override fun initViewBinding(): ActivityScrollTextBinding {
        return ActivityScrollTextBinding.inflate(layoutInflater)
    }

    var totalMove = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.view.post {
            slidingHelper = SlidingHelper.create(binding.view, object : SlidingHelper.OnSlideFinishListener {
                override fun onSlideFinished() {
                }

                override fun onSliding(angle: Float) {
                    binding.view.rotation += angle
                    totalMove += angle
                    Log.i("scrollText", totalMove.toString())
                }
            })
            slidingHelper?.enableInertialSliding(true)
        }
        window.decorView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                slidingHelper?.handleMovement(event)
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        slidingHelper?.release()
    }

    override fun onClick(v: View?) {
        when (v) {
        }
    }
}