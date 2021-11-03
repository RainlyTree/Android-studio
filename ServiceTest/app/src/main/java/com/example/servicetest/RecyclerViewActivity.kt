package com.example.servicetest

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var list: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        initData()
        initView()
    }

    fun initView() {
        recyclerView = findViewById(R.id.recycler_view)
        val adapter = list?.let { HomeAdapter(it, this) }
        recyclerView?.adapter = adapter
        adapter?.setOnItemClickLister(object : HomeAdapter.OnItemClickLister{
            override fun OnItemClick(view: View, position: Int) {
                Toast.makeText(this@RecyclerViewActivity, "点击了第" + (position + 1) + "条信息", Toast.LENGTH_SHORT).show()
            }

            override fun OnItemLongClick(view: View, position: Int) {
                AlertDialog.Builder(this@RecyclerViewActivity)
                    .setTitle("确认删除吗")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() {
                            dialogInterface: DialogInterface, i: Int ->
                        adapter.removeData(position)
                    }).show()
            }

        })
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.addItemDecoration(DividerItemDecoration(this, orientationEnum.VERTICAL_LIST))
        recyclerView?.itemAnimator = DefaultItemAnimator()
    }

    fun initData() {
        list = ArrayList<String>(100)
        for(i in 1..100) {
            list?.add(i.toString())
        }
    }
}