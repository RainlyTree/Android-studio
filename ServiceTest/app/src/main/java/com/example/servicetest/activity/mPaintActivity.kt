package com.example.servicetest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.servicetest.R
import com.example.servicetest.activity.baseActivity.BaseActivity
import com.example.servicetest.databinding.ActivityMPaintBinding

class mPaintActivity : BaseActivity<ActivityMPaintBinding>() {

    override fun initViewBinding(): ActivityMPaintBinding {
        return ActivityMPaintBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}