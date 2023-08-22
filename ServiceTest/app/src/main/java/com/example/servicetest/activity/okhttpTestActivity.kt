package com.example.servicetest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.servicetest.databinding.ActivityOkhttpTestBinding
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit

class okhttpTestActivity : AppCompatActivity() {

    private var _binding : ActivityOkhttpTestBinding? = null
    val binding : ActivityOkhttpTestBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOkhttpTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWww.setOnEditorActionListener { v, _, _ ->
            syncGet(v.text.toString())
            binding.tvWww.text.clear()
            false
        }

        postClient()
        postText()
    }

    //Get请求  异步请求
    fun getClent(url: String) {
        val handler = Handler(mainLooper)
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

    fun postText() {
        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("username", "wildma")
            .add("password", "123456")
            .build()
        val request = Request.Builder()
            .url("https://postman-echo.com/post")
            .post(body)
            .build()
        getRequestLog(request)
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    getResponseLog(response)
                }
            }

        })
    }

    val TAG = "syncGet"
    private val UTF8 = Charset.forName("UTF-8")

    fun syncGet(url: String) {
        val okhttpClient = OkHttpClient().newBuilder()
            .connectTimeout(10L, TimeUnit.SECONDS)  //设置超时时间
            .writeTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(1L, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder().url("http://$url").build()
        getRequestLog(request)

        lifecycleScope.launch(Dispatchers.IO) {
            val response = okhttpClient.newCall(request).execute()
            getResponseLog(response)
        }
    }


    fun getRequestLog(request: Request) {
        var body: String? = null
        val requestBody = request.body
        requestBody?.let {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset: Charset? = UTF8
            val contentType = requestBody.contentType()
            contentType?.let {
                charset = contentType.charset(UTF8)
            }
            body = buffer.readString(charset!!)
        }
        Log.i(TAG,
            "发送请求: method：" + request.method
                    + "\nurl：" + request.url
                    + "\n请求头：" + request.headers
                    + "\n请求参数: " + body)
    }

    fun getResponseLog(response: Response) {
        if (response.isSuccessful) {
            val responseBody = response.body
            var body: String? = null
            val requestBody = response.request.body
            requestBody?.let {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                var charset: Charset? = UTF8
                val contentType = requestBody.contentType()
                contentType?.let {
                    charset = contentType.charset(UTF8)
                }
                body = buffer.readString(charset!!)
            }
            val rBody: String

            val source = responseBody!!.source()
            source.request(java.lang.Long.MAX_VALUE)
            val buffer = source.buffer()

            var charset: Charset? = UTF8
            val contentType = responseBody.contentType()
            contentType?.let {
                try {
                    charset = contentType.charset(UTF8)
                } catch (e: UnsupportedCharsetException) {
                    Log.e(TAG, e.message ?:"")
                }
            }
            rBody = buffer.clone().readString(charset!!)
            Log.i(TAG,
                "收到响应: code:" + response.code
                        + "\n请求url：" + response.request.url
                        + "\n请求body：" + body
                        + "\nResponse: " + rBody)
        }
    }
}