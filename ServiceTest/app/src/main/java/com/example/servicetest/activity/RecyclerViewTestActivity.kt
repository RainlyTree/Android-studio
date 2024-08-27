package com.example.servicetest.activity

import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servicetest.activity.baseActivity.BaseActivity
import com.example.servicetest.adapter.MoveAdapter
import com.example.servicetest.databinding.ActivityRecyclerviewTestBinding
import com.example.servicetest.helpUtils.mItemTouchHelperCallback

class RecyclerViewTestActivity : BaseActivity<ActivityRecyclerviewTestBinding>(){

    var mAdapter : MoveAdapter = MoveAdapter()

    override fun initViewBinding(): ActivityRecyclerviewTestBinding {
        return ActivityRecyclerviewTestBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //实例化callback
        val callback = mItemTouchHelperCallback(mAdapter)
        //创建ItemTouchHelper
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.rcyMes)
        binding.rcyMes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                callback.clearDeleteButton()
            }
        })
        if (mAdapter.mData == null) {
            mAdapter.mData = arrayListOf()
        }
        for (i in 0..20) {
            mAdapter.mData!!.add("Item ${i}")
        }
        binding.rcyMes.adapter = mAdapter
        binding.rcyMes.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
    }
}