package com.example.servicetest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.servicetest.databinding.ActivityOkhttpTestBinding
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class okhttpTestActivity : AppCompatActivity() {

    private var _binding : ActivityOkhttpTestBinding? = null
    val binding : ActivityOkhttpTestBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOkhttpTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWww.setOnEditorActionListener { v, _, _ ->
            getClent(v.text.toString())
            binding.tvWww.text.clear()
            false
        }

        postClient()


//        //创建客户端对象
//        val client = OkHttpClient()
//        //创建一个Request对象 异步使用
//        val request = Request.Builder().url("www.baidu.com").build()
//        //进行请求
//        //同步请求
//        val response = client.newCall(request).execute()
//        //异步请求
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

    //Get请求
    fun getClent(url: String) {
        val handler = Handler(Looper.getMainLooper())
        val httpClient = OkHttpClient()
        val urls = "https://$url"
        val request = Request.Builder()
            .url(urls)
            .get()
            .build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //当前在子进程 需要切到主线程进行UI处理  不然就崩了
                handler.post(Runnable {
                    binding.tvContent.text = e.toString()
                })
            }

            override fun onResponse(call: Call, response: Response) {
                handler.post(Runnable {
                    binding.tvContent.text = response.body.toString()
                })
            }

        })
    }

    //Post请求
    fun postClient() {
        val handler = Handler(Looper.getMainLooper())
        val httpClient = OkHttpClient()

        //需要多构造一个RequestBody对象
        val contentType = "text/x-markdown; charset=utf-8".toMediaTypeOrNull()
        val content = "这是一个content"
        val body = content.toRequestBody(contentType)

        val postRequest = Request.Builder()
            .url("https://api.github.com/markdown/raw")
            .post(body)
            .build()
        val call = httpClient.newCall(postRequest)
        call.enqueue(object :Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                handler.post {
                    binding.tvContent.text = response.toString() + "\n" + response.body?.string()
                }
            }

        })
    }
}