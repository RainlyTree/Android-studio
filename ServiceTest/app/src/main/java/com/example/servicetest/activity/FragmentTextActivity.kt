package com.example.servicetest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.servicetest.R
import com.example.servicetest.databinding.ActivityFragmentTextBinding

class FragmentTextActivity : AppCompatActivity() {

    var _bingding: ActivityFragmentTextBinding? = null
    val binding get() = _bingding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _bingding = ActivityFragmentTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.rightLayout, fragment)
        //将fragment记录  点Back 返回到上一个fragment
        transaction.addToBackStack(null)
        transaction.commit()
    }
}