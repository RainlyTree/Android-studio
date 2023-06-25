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

    }

    //Get请求  异步请求
    fun getClent(url: String) {
        val handler = Handler(Looper.getMainLooper())
        val httpClient = OkHttpClient()
        val urls = "http://$url"
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

//        //同步请求玩法
//        val client = OkHttpClient()
//        //创建一个Request对象
//        val request = Request.Builder().url("www.baidu.com").build()
//        //同步请求
//        val response = client.newCall(request).execute()
//        response.body.toString()
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