package com.example.servicetest.activity.baseActivity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>: AppCompatActivity() {

    private var _binding: T? = null
    val binding: T
        get() = _binding!!

    abstract fun initViewBinding(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = initViewBinding()
        setContentView(binding.root)
    }


    //另外一种方式
    inline fun <reified T: Activity> Activity.binding() = lazy {
        T::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(layoutInflater) as T
    }
}