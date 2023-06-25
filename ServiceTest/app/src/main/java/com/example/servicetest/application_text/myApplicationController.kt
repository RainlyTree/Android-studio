package com.example.servicetest.application_text

import android.app.Application
import android.util.Log

class myApplicationController : Application() {

    //只有仿真机测试会触发， Android产品机不会触发
    //是不是可以用作判断是否是模拟器??  不可以 G
    override fun onTerminate() {
        super.onTerminate()
        Log.i("application", "exit")
    }
}