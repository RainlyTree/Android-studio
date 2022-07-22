package com.example.servicetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import com.example.servicetest.databinding.ActivityOkhttpTestBinding
import kotlinx.coroutines.*
import okhttp3.*
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

    fun getClent(url: String) {
        val httpClient = OkHttpClient()
        val urls = "https://$url"
        val request = Request.Builder().url(urls).get().build()
        val call = httpClient.newCall(request)
//        httpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                binding.tvContent.text = e.toString()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                binding.tvContent.text = response.body.toString()
//            }
//
//        })

        CoroutineScope(Dispatchers.IO).launch {
            val response = call.execute()
            withContext(Dispatchers.Main) {
                binding.tvContent.text = response.body.toString()
            }
        }
    }
}