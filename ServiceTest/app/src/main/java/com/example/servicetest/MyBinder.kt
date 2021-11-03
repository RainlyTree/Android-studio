package com.example.servicetest

import android.os.Binder
import android.util.Log

/**
 *  Created by chenlin on 2021/9/18.
 */
class MyBinder : Binder() {

    fun startDownload() {
        Thread(Runnable {
            Log.d("MyBinder", "Thread is"+Thread.currentThread().id)
            Log.d("MyBinder", "start downLoad")
            for (i in 1..10) {
                var string = ""
                for (j in 1..i) {
                    string += "*"
                }
                Log.d("MyBinder", string)
            }
            Log.d("MyBinder", "downLoad finish over!!!!")
        }).start()
    }
}