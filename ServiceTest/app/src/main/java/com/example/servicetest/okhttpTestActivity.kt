package com.example.servicetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import okhttp3.*
import java.io.IOException

class okhttpTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_okhttp_test)


        //创建客户端对象
        val client = OkHttpClient()
        //创建一个Request对象 异步使用
        val request = Request.Builder().url("www.baidu.com").build()
        //进行请求
        //同步请求
        val response = client.newCall(request).execute()
        //异步请求
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Toast.makeText(this@okhttpTestActivity, "QWQ 麻了 还有问题", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                Toast.makeText(this@okhttpTestActivity, response.message, Toast.LENGTH_SHORT).show()
//            }
//
//        })
    }
}