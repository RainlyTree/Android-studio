package com.example.servicetest.recyclerViewManager

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.Scroller
import androidx.annotation.FloatRange
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class SlidingHelper(context: Context, pivotX: Float, pivotY: Float, listener: OnSlideFinishListener) {

    var mStartX = 0f
    var mStartY = 0f
    var mPivotX = pivotX
    var mPivotY = pivotY

    var isRecycled = false      //资源是否已经释放
    var isInertialSlidingEnable = false     //是否开启惯性滑动
    var isYMove = false     //上下滑动
    private var mSlideFinishListener: OnSlideFinishListener? = listener //滑动监听
    private var mScrollAvailabilityRatio = 0f   //滑动率设置
    private var isClockwiseScrolling = false    //是否是顺时针滑动
    private var mHandler: InertialSlidingHandler? = InertialSlidingHandler(this)
    private var isSelfSliding = false

    private var mVelocityTracker: VelocityTracker? = VelocityTracker.obtain()
    private var mScroller: Scroller? = Scroller(context)

    init {
        mScrollAvailabilityRatio = .3f
    }

    fun setScrollAvailabilityRatio(@FloatRange(from = 0.0, to = 1.0) ratio: Float) {
        checkIsRecycled()
        mScrollAvailabilityRatio = ratio
    }

    fun handleMovement(event: MotionEvent) {
        val x = if (isSelfSliding) {event.rawX} else event.x
        val y = if (isSelfSliding) {event.rawY} else event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mScroller?.isFinished == false) {
                    mScroller?.abortAnimation()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                handleActionMove(x, y)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                if (isInertialSlidingEnable) {
                    mVelocityTracker?.computeCurrentVelocity(1000)
                    mScroller?.fling(
                        0,
                        0,
                        mVelocityTracker?.xVelocity?.toInt() ?: 0,
                        mVelocityTracker?.yVelocity?.toInt() ?: 0,
                        Int.MIN_VALUE,
                        Int.MAX_VALUE,
                        Int.MIN_VALUE,
                        Int.MAX_VALUE
                    )
                    startFling()
                }
            }
        }
        mVelocityTracker?.addMovement(event)
        mStartY = y
        mStartX = x
    }

    /**
     * 是否是顺时针滑动
     */
    private fun isClockwise(x: Float, y: Float): Boolean {
        isYMove = abs(y - mStartY) > abs(x - mStartX)
        return if (isYMove) {
            x < mPivotX != y > mStartY
        } else {
            y < mPivotY == x > mStartX
        }
    }

    /**
     * 计算滑动的角度
     */
    fun handleActionMove(x: Float, y: Float) {
        var l = 0f
        var t = 0f
        var r = 0f
        var b = 0f
        if (mStartX > x) {
            r = mStartX
            l = x
        } else {
            l = mStartX
            r = x
        }
        if (mStartY > y) {
            b = mStartY
            t = y
        } else {
            t = mStartY
            b = y
        }
        val pA1 = abs(mStartX - mPivotX)
        val pA2 = abs(mStartY - mPivotY)
        val pB1 = abs(x - mPivotX)
        val pB2 = abs(y - mPivotY)
        val hypotenuse = sqrt((r - l).pow(2) + (t - b).pow(2))
        val lineA = sqrt(pA1.pow(2) + pA2.pow(2))
        val lineB = sqrt(pB1.pow(2) + pB2.pow(2))
        val angle = fixAngle(Math.toDegrees(
            acos(
                (lineA.toDouble().pow(2.0) + lineB.toDouble().pow(2.0) - hypotenuse.toDouble()
                    .pow(2.0)) / (2 * lineA * lineB)
            )).toFloat()
        )
        if (!angle.isNaN()) {
            isClockwiseScrolling = isClockwise(x, y)
            mSlideFinishListener?.onSliding(if (isClockwiseScrolling) angle else -angle)
        }
    }

    /**
     * 调整角度 让他在360度内
     */
    fun fixAngle(rotation: Float): Float {
        var fixRotation = rotation
        val angle = 360f
        if (rotation < 0) {
            fixRotation += angle
        }
        if (rotation > angle) {
            fixRotation = rotation % angle
        }
        return fixRotation
    }

    public fun enableInertialSliding(enable: Boolean) {
        checkIsRecycled()
        isInertialSlidingEnable = enable
    }

    private fun checkIsRecycled() {
        if (isRecycled) {
            throw IllegalStateException("SlidingHelper is recycled!")
        }
    }

    var mLastScrollOffset = 0f
    /**
     * 处理惯性滑动
     */
    private fun computeInertialSliding() {
        checkIsRecycled()
        if (mScroller?.computeScrollOffset() == true) {
            val y = (if (isYMove) mScroller?.currY else mScroller?.currX)?.times(
                mScrollAvailabilityRatio
            ) ?: 0f
            if (mLastScrollOffset != 0f) {
                val offset = fixAngle(abs(y - mLastScrollOffset))
                mSlideFinishListener?.onSliding(if (isClockwiseScrolling) offset else -offset)
            }
            mLastScrollOffset = y
            startFling()
        } else if (mScroller?.isFinished == true) {
            mLastScrollOffset = 0f
            mSlideFinishListener?.onSlideFinished()
        }
    }

    fun setOnSlideFinishListener(listener: OnSlideFinishListener) {
        mSlideFinishListener = listener
    }

    /**
     * 释放资源
     */
    fun release() {
        checkIsRecycled()
        mScroller = null
        mVelocityTracker?.recycle()
        mVelocityTracker = null
        mHandler = null
        isRecycled = true
    }

    /**
     * 开始惯性滚动
     */
    private fun startFling() {
        mHandler?.sendEmptyMessage(0)
    }

    /**
     * 更新圆心x坐标
     *
     * @param pivotX 新的x坐标
     */
    fun updatePivotX(pivotX: Int) {
        checkIsRecycled()
        mPivotX = pivotX.toFloat()
    }

    /**
     * 更新圆心y坐标
     *
     * @param pivotY 新的y坐标
     */
    fun updatePivotY(pivotY: Int) {
        checkIsRecycled()
        mPivotY = pivotY.toFloat()
    }

    /**
     * 设置自身滑动
     *
     * @param isSelfSliding 是否view自身滑动
     */
    fun setSelfSliding(isSelfSliding: Boolean) {
        checkIsRecycled()
        this.isSelfSliding = isSelfSliding
    }

    /**
     * 监听滚动完毕
     */
    interface OnSlideFinishListener {
        /**
         * 滚动完毕
         */
        fun onSlideFinished()

        /**
         * @param angle 本次滑动的角度
         */
        fun onSliding(angle: Float)
    }

    companion object {

        fun create(targetView: View, listener: OnSlideFinishListener): SlidingHelper {
            var width = targetView.width
            var height = targetView.height
            if (width == 0 || height == 0) {
                //有问题 需要调用updatePivotX updatePivotY
            }
            width /= 2
            height /= 2
            val array = IntArray(2)
            targetView.getLocationOnScreen(array)
            return SlidingHelper(targetView.context, array[0].toFloat() + width, array[1].toFloat() + height, listener)
        }

        class  InertialSlidingHandler(var mHelper: SlidingHelper): Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                mHelper.computeInertialSliding()
            }
        }
    }
}