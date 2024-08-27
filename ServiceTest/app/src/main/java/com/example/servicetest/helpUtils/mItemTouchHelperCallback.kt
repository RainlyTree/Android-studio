package com.example.servicetest.helpUtils

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * 支持侧滑删除 上下拖动
 */
class mItemTouchHelperCallback(var mAdapter: ItemTouchHelperAdapter? = null): ItemTouchHelper.Callback() {

    fun clearDeleteButton() {
        lastTouchHolder?.itemView?.scrollTo(0, 0)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        //重置设置 防止复用出问题
        if (viewHolder.itemView.scrollX > getSlideLimitation(viewHolder) / 2) {
            viewHolder.itemView.scrollTo(getSlideLimitation(viewHolder), 0)
        } else {
            viewHolder.itemView.scrollTo(0, 0)
        }
    }

    var lastTouchHolder: ViewHolder? = null

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (lastTouchHolder != viewHolder) {
            lastTouchHolder?.itemView?.scrollTo(0, 0)
            lastTouchHolder = viewHolder
        }
        if (isCurrentlyActive) {
            //手指滑动中
            if (viewHolder. itemView.scrollX != getSlideLimitation(viewHolder)) {
                viewHolder.itemView.scrollTo((-dX).toInt().coerceAtMost(getSlideLimitation(viewHolder)), 0)
            }
        } else {
            //动画滑动
            if (viewHolder.itemView.scrollX >= getSlideLimitation(viewHolder)) {
                viewHolder.itemView.scrollTo(
                    (-dX.toInt()).coerceAtLeast(
                        getSlideLimitation(viewHolder)
                    ), 0)
            } else {
                viewHolder.itemView.scrollTo(viewHolder.itemView.scrollX, 0)
            }
        }
    }

    fun getSlideLimitation(viewHolder: ViewHolder): Int {
        return (viewHolder.itemView as ViewGroup).getChildAt(1).layoutParams.width
    }


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN  //可以上下拖动
        val swipeFlags = ItemTouchHelper.LEFT
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //onItemMove
        mAdapter?.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mAdapter?.oniTemDismiss(viewHolder.adapterPosition)
    }

    //防止侧滑删除 将阈值开到最大
    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        return Float.MAX_VALUE
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Float.MAX_VALUE
    }

}