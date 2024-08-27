package com.example.servicetest.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.servicetest.R
import com.example.servicetest.activity.baseActivity.BaseActivity
import com.example.servicetest.databinding.ActivityFanLayoutTextBinding

class FanLayoutTextActivity : BaseActivity<ActivityFanLayoutTextBinding>(), View.OnClickListener {

    override fun initViewBinding(): ActivityFanLayoutTextBinding {
        return ActivityFanLayoutTextBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.iv1.setOnClickListener(this)
        binding.iv2.setOnClickListener(this)
        binding.iv3.setOnClickListener(this)
        binding.iv4.setOnClickListener(this)
        binding.iv5.setOnClickListener(this)
        binding.iv6.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.iv1 -> {
                Toast.makeText(baseContext, "iv1", Toast.LENGTH_SHORT).show()
            }
            binding.iv2 -> {
                Toast.makeText(baseContext, "iv2", Toast.LENGTH_SHORT).show()
            }
            binding.iv3 -> {
                Toast.makeText(baseContext, "iv3", Toast.LENGTH_SHORT).show()
            }
            binding.iv4 -> {
                Toast.makeText(baseContext, "iv4", Toast.LENGTH_SHORT).show()
            }
            binding.iv5 -> {
                Toast.makeText(baseContext, "iv5", Toast.LENGTH_SHORT).show()
            }
            binding.iv6 -> {
                Toast.makeText(baseContext, "iv6", Toast.LENGTH_SHORT).show()
            }
        }
    }
}