package com.example.servicetest.helpUtils

interface ItemTouchHelperAdapter {
    //数据交换
    fun onItemMove(fromPosition: Int, toPosition: Int)
    //数据删除
    fun oniTemDismiss(position: Int)
}