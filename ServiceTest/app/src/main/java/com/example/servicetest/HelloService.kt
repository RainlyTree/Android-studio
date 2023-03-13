package com.example.servicetest

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.servicetest.activity.MainActivity


/**
 *  Created by chenlin on 2021/9/18.
 */
class HelloService : Service() {

    var mStartMode = 0  //标记服务被kill后行为

    var mBinder: MyBinder? = MyBinder()    //绑定的客户端接口

    var mAllowRebind: Boolean = false   //是否可以使用onRebind


    companion object {
        val TAG = "HelloService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread(Runnable {
            Log.d(TAG, "onStartCommand Thread is" + Thread.currentThread().id)
            Log.i(TAG, "onStartCommand()")
        }).start()
        Toast.makeText(this, "服务启动", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, "normal")
            .setContentTitle("this is content title")
            .setContentText("this is content text")
            .setContentIntent(pi)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .build()
        manager.notify(1, notification)

        Log.i(TAG, "oncreate()")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG, "onBind()")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onunBind()")
        return mAllowRebind
    }

    override fun onRebind(intent: Intent?) {
        Log.i(TAG, "onRebind()")
        super.onRebind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy()")
        Toast.makeText(this, "服务已经停止", Toast.LENGTH_SHORT).show()
    }

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            stopSelf(msg.arg1)
        }
    }
}