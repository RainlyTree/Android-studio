package com.example.servicetest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.servicetest.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    var _binding: ActivitySecondBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySecondBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.contentLayout.tvSecond.text = intent.getStringExtra("extraInfo")

        val intent = Intent().apply {
            putExtra("ResultData", "我回传回来了 快来接收一下")
        }
        setResult(Activity.RESULT_OK, intent)
    }

    override fun onDestroy() {
        val intent = Intent().apply {
            putExtra("ResultData", "我回传回来了 快来接收一下")
        }
        setResult(Activity.RESULT_OK, intent)
        super.onDestroy()
    }
}