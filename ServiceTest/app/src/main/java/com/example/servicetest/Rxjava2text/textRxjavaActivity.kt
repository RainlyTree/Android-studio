package com.example.servicetest.Rxjava2text

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.servicetest.R
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class textRxjavaActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "textRxjavaActivity"
    }

    var mDisposable : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_rxjava)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    /**
     * 动态代理
     */
    //接口
    interface Dao {
        fun save()
    }

    //实现类
    class UserDao : Dao {
        override fun save() {
            println("Good")
        }
    }

    //代理类
    class ProxyFactory(var target: Any) {
        fun getProxyInstance(): Any {
            return Proxy.newProxyInstance(target.javaClass.classLoader, target.javaClass.interfaces
            ) { proxy, method, args ->
                println("开始了哦")
                val returnVal = method.invoke(target, args)
                println("结束了哦")
                returnVal!!
            }
        }
    }


    //基础的观察者
    fun text1() {
        val novel = Observable.create(ObservableOnSubscribe<String> { emitter ->
            emitter.onNext("1")
            emitter.onNext("2")
            emitter.onNext("3")
            emitter.onComplete()
        })

        val reader = object :Observer<String> {
            override fun onSubscribe(d: Disposable) {
                //这里可以取消订阅
                //d.dispose()
                mDisposable = d
            }
            override fun onNext(t: String) {
                when (t) {
                    "2" -> {
                        mDisposable?.dispose()
                        return
                    }
                    else -> Log.i(TAG, t)
                }
            }
            override fun onError(e: Throwable) {
                Log.i(TAG, e.message ?: "")
            }

            override fun onComplete() {
                Log.i(TAG, "onComplete")
            }
        }
        novel.subscribe(reader)
    }

    fun text2() {
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            emitter.onNext("1")
            emitter.onNext("2")
            emitter.onNext("3")
            emitter.onComplete()
        }).observeOn(AndroidSchedulers.mainThread())    //回调在主线程
            .subscribeOn(Schedulers.io())   //执行在io线程
            .subscribe()
    }
}