package com.example.servicetest

import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.webkit.*
import android.widget.RemoteViews
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.servicetest.activity.TextInputLayoutActivity
import com.example.servicetest.activity.mPaintActivity
import com.example.servicetest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import permissions.dispatcher.*
import permissions.dispatcher.PermissionRequest

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!
    var connection: ServiceConnection? = null
    var webView: WebView? = null

    var myAL: ActivityResultLauncher<Intent>? = null

    //广播相关
    var intentFilter: IntentFilter? = null
    var localReceiver: LocalReceiver? = null
    var localBroadcastManager: LocalBroadcastManager? = null

    companion object {
        val PERMISSIONS_REQUEST_CALL_PHONE = 1

        val TAG = "MainActivity + "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")
        webView = WebView(this)
        webView?.loadUrl("http://www.baidu.com")
        webView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        //ToolBar设置
        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.action_setting -> {
                    Toast.makeText(this, "action_setting", Toast.LENGTH_SHORT).show()
                    return@setOnMenuItemClickListener true
                }
                R.id.action_share -> {
                    Toast.makeText(this, "action_share", Toast.LENGTH_SHORT).show()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }

        val mDrawerToggle = ActionBarDrawerToggle(this, binding.lidDrawerLayout, binding.toolbar.toolbar, 0, 0)
        mDrawerToggle.syncState()
        binding.lidDrawerLayout.addDrawerListener(mDrawerToggle)
        binding.tvClose.setOnClickListener {
            binding.lidDrawerLayout.closeDrawer(Gravity.LEFT)
        }

        //设置跳转返回内容
        myAL = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.data != null && it.resultCode == Activity.RESULT_OK) {
                it.data?.getStringExtra("ResultData")?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show()
            }
        }

        //获取广播实例
    }

    fun startService(view: View) {
        startService(Intent(baseContext, HelloService::class.java))
    }

    fun stopService(view: View){
        stopService(Intent(baseContext, HelloService::class.java))
    }

    fun bindService(view: View) {
        connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val myBinder: MyBinder = service as MyBinder
                myBinder.startDownload()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }

        }
        bindService(Intent(baseContext, HelloService::class.java), connection as ServiceConnection, BIND_AUTO_CREATE)
    }

    fun unbindService(view: View) {
        connection?.let {
            unbindService(it)
        }
    }

    fun openWebAcitivity(view: View) {
        val intent = Intent(this, WebActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun simpleNotification(view: View) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "default")
        val mIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"))
        val pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .setContentTitle("普通通知")
        val notifacationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Simple", NotificationManager.IMPORTANCE_DEFAULT)
            notifacationManager.createNotificationChannel(channel)
        }
        notifacationManager.notify(1, builder.build())
    }

    fun selfNotification(view: View) {
        val remoteView = RemoteViews(packageName, R.layout.view_fold)
        val builder = NotificationCompat.Builder(this, "self")
        val mIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"))
        val pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .setContentTitle("折叠式通知，打开是自定义的remoteview")
        val notifacationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = builder.build()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("self", "self", NotificationManager.IMPORTANCE_DEFAULT)
            notifacationManager.createNotificationChannel(channel)
        }
        notification.bigContentView = remoteView
        notifacationManager.notify(2, notification)
    }

    fun suspendNotification(view: View) {
        val builder = NotificationCompat.Builder(this, "suspend")
        val mIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"))
        val pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .setContentTitle("悬挂式通知")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val intent = Intent(this, MainActivity::class.java)
        val hangPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setFullScreenIntent(hangPendingIntent, true)

        val notifacationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("suspend", "suspend", NotificationManager.IMPORTANCE_HIGH)
            notifacationManager.createNotificationChannel(channel)
        }
        notifacationManager.notify(3, builder.build())
    }

    @NeedsPermission(android.Manifest.permission.CALL_PHONE)
    fun call(view: View) {
        val permission = Array<String>(1) {android.Manifest.permission.CALL_PHONE}
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permission, PERMISSIONS_REQUEST_CALL_PHONE)
        } else {
            callPhone()
        }
    }

    fun Openrecycler(view: View) {
        val intent = Intent(this, RecyclerViewActivity::class.java)
        startActivity(intent)
    }

    fun openRecyclerGrid(view: View) {
        val intent = Intent(this, recyclerViewGrid::class.java)
        startActivity(intent)
    }

    fun openSnackbar(view: View) {
        Snackbar.make(binding.root, "标题", Snackbar.LENGTH_LONG)
            .setAction("点击事件") {
                Toast.makeText(this, "Toast", Toast.LENGTH_SHORT).show()
            }.setDuration(Snackbar.LENGTH_LONG).show()
    }

    fun openActivity(view: View) {
        val intent = Intent("com.intent.activitytext.ACTION_START")
        intent.putExtra("extraInfo", "这是传输过来的额外消息")
        myAL!!.launch(intent)
    }

    fun openFragment(view: View) {
        val intent = Intent(this, FragmentTextActivity::class.java)
        startActivity(intent)
    }

    fun openDynamicBroadCast(view: View) {
        val intent = Intent(this, BroadCastTest::class.java)
        startActivity(intent)
    }

    fun openCardView(view: View) {
        val intent = Intent(this, CardViewText::class.java)
        startActivity(intent)
    }

    fun openViewPager2Activity(view: View) {
        val intent = Intent(this, viewPager2Acitivity::class.java)
        startActivity(intent)
    }

    fun openOkHttpActivity(view: View) {
        val intent = Intent(this, okhttpTestActivity::class.java)
        startActivity(intent)
    }

    fun openPaintActivity(view: View) {
        val intent = Intent(this, mPaintActivity::class.java)
        startActivity(intent)
    }

    fun openTextInputLayout(view: View) {
        val intent = Intent(this, TextInputLayoutActivity::class.java)
        startActivity(intent)
    }

    fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:" + "10086")
        intent.setData(data)
        try {
            startActivity(intent)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    @OnShowRationale(android.Manifest.permission.CALL_PHONE)
    fun showWhy(request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setMessage("提醒用户为什么开启此权限")
            .setPositiveButton("知道了", DialogInterface.OnClickListener() {
                _, _ -> request.proceed()
            }).show()
    }

    @OnPermissionDenied(android.Manifest.permission.CALL_PHONE)
    fun showDenied() {
        Toast.makeText(this, "用户选择拒绝给出的提示", Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(android.Manifest.permission.CALL_PHONE)
    fun showNotAsk() {
        AlertDialog.Builder(this)
            .setMessage("该功能需要访问电话的权限，不开启将无法正常工作！")
            .setPositiveButton("确定", DialogInterface.OnClickListener() {
                _, _ ->
            }).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSIONS_REQUEST_CALL_PHONE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone()
            } else {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
                    val dialog = AlertDialog.Builder(this)
                        .setMessage("该功能需要访问电话权限")
                        .setPositiveButton("确定", DialogInterface.OnClickListener() {
                            _,_ ->
                        }).create()
                    dialog.show()
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "OnStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestory")
    }

}

