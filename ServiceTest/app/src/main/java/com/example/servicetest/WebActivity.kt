package com.example.servicetest

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.*
import android.widget.TextView

class WebActivity : AppCompatActivity() {

    var webView: WebView? = null
    var tvTitle: TextView? = null
    var tvStart: TextView? = null
    var tvProgress: TextView? = null
    var tvEnd: TextView? = null
    var mProgressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        tvTitle = findViewById(R.id.tv_title)
        tvStart = findViewById(R.id.tv_start)
        tvProgress = findViewById(R.id.tv_progress)
        tvEnd = findViewById(R.id.tv_end)
        webView = findViewById(R.id.wv_webview)
        mProgressDialog = AlertDialog.Builder(this).create()

        webView?.loadUrl("http://www.baidu.com/")
        webView?.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if(request?.url.toString().contains("csdn.net")) {
                    view?.loadUrl("http://www.baidu.com")
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                tvStart?.text = "开始加载！！"
                mProgressDialog?.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                tvEnd?.text = "加载完成"
                mProgressDialog?.hide()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.i("onReceivedError", "http 有点问题")
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                Log.i("onReceivedSslError", "https 有点问题")
                super.onReceivedSslError(view, handler, error)
            }
        }
        webView?.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                tvTitle?.text = title
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if(newProgress < 100) {
                    tvProgress?.text = (newProgress.toString() + "%")
                } else {
                    tvProgress?.text = "100%"
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView?.canGoBack() ?: false) {
            webView?.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        if(webView != null) {
            webView?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView?.clearHistory()

            val viewGroup = (webView!!.parent) as ViewGroup
            viewGroup.removeView(webView)
            webView!!.destroy()
            webView = null
        }
        super.onDestroy()
    }
}