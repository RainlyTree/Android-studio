package com.example.servicetest

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class recyclerViewGrid : AppCompatActivity() {

    var recyclerViewGrid: RecyclerView? = null

    val recyclerView get() = recyclerViewGrid!!
    var mList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_grid)

        initData()
        initView()
    }

    private fun initView() {
        recyclerViewGrid = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        //用来实现瀑布流效果
        val mHeights = ArrayList<Int>()
        for(i in 0..99) {
            mHeights?.add((100 + Math.random() * 300).toInt())
        }
        val adapter = mList?.let { HomeAdapter(it, this) }
        adapter?.mHeights = mHeights
        adapter?.setOnItemClickLister(object : HomeAdapter.OnItemClickLister{
            override fun OnItemClick(view: View, position: Int) {
                Toast.makeText(this@recyclerViewGrid, "点击了第" + (position + 1) + "条信息", Toast.LENGTH_SHORT).show()
            }

            override fun OnItemLongClick(view: View, position: Int) {
                AlertDialog.Builder(this@recyclerViewGrid)
                    .setTitle("确认删除吗")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() {
                            dialogInterface: DialogInterface, i: Int ->
                        adapter.removeData(position)
                        Log.i("123", "$position")
                    }).show()
            }

        })
        recyclerView.adapter = adapter
    }

    fun initData() {
        mList = ArrayList<String>(100)
        for(i in 1..100) {
            mList!!.add(i.toString())
        }
    }
}