package com.example.servicetest.scrollText

import android.content.Context
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.view.ViewConfigurationCompat

class ScrollerLayout(context: Context): ViewGroup(context) {

    /**
     * 用于完成滚动操作实例
     */
    private var mScroller: Scroller? = null

    /**
     * 判定拖动的最小移动像素数
     */
    private var mTouchSlop: Int? = null

    /**
     * 手机按下的屏幕坐标
     */
    private var mXDown: Float? = null
    private var mYDown: Float? = null

    /**
     * 手机当时所处的屏幕坐标
     */
    private var mXMove: Float? = null

    /**
     * 上次触发ACTION_MOVE事件的屏幕坐标
     */
    private var mXLastMove: Float? = null

    /**
     * 界面可滚动的左边界
     */
    private var leftBorder: Int? = null

    /**
     * 界面可滚动的右边界
     */
    private var rightBorder: Int? = null

    init {
        mScroller = Scroller(context)
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledPagingTouchSlop
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            for (i in 0 until childCount) {
                val childView = getChildAt(i)
                childView.layout(i * childView.measuredWidth, 0, (i + 1) * childView.measuredWidth, childView.measuredHeight)
            }
            leftBorder = getChildAt(0).left
            rightBorder = getChildAt( childCount - 1).right
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mXDown = ev.rawX
                mXLastMove = mXDown
            }
            MotionEvent.ACTION_MOVE -> {
                mXMove = ev.rawX
                val diff = Math.abs(mXMove!! - mXDown!!)
                mXLastMove = mXMove
                //这玩意真的搞
                if (diff > mTouchSlop!!) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                mXMove = event.rawX
                val scrolledX = mXLastMove!! - mXMove!!
                if (scrollX + scrolledX < leftBorder!!) {
                    scrollTo(leftBorder!!, 0)
                    return true
                } else if (scrollX + width + scrolledX > rightBorder!!) {
                    scrollTo(rightBorder!! - width, 0)
                    return true
                }
                scrollBy(scrolledX.toInt(), 0)
                mXLastMove = mXMove
            }
            MotionEvent.ACTION_UP -> {
                //当手指抬起，根据当前的滚动值来判断应该滚动到哪个子控件
                val targetIndex = (scrollX + width / 2) / width
                val dx = targetIndex + width - scrollX
                //调用startScroll()初始化滚动数据并刷新页面
                mScroller?.startScroll(scrollX, 0, dx, 0)
            }
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (mScroller?.computeScrollOffset() == true) {
            scrollTo(mScroller?.currX!!, mScroller?.currY!!)
            invalidate()
        }
    }
}