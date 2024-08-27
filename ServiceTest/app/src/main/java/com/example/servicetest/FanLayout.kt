package com.example.servicetest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.IntDef
import com.example.servicetest.recyclerViewManager.SlidingHelper
import kotlin.math.abs

class FanLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs),
    SlidingHelper.OnSlideFinishListener {

    companion object {
        private const val TYPE_COLOR = 0    //color类型
        private const val TYPE_VIEW = 1     //View类型
        private const val LEFT = 0
        private const val RIGHT = 1
        private const val TOP = 2
        private const val BOTTOM = 3
        private const val LEFT_TOP = 4
        private const val LEFT_BOTTOM = 5
        private const val RIGHT_TOP = 6
        private const val RIGHT_BOTTOM = 7

        private var mCurrentGravity = -1    //当前的对齐方式

        var MODE_AVERAGE = 0 //平均分布
        var MODE_FIXED = 1  //制定角度

        private var mItemLayoutMode = 0     //item布局模式
        private var mItemAngleOffset = 0f    //item角度偏移量


        @Target(AnnotationTarget.VALUE_PARAMETER)
        @IntDef(LEFT, RIGHT, TOP, BOTTOM, LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Gravity {
        }
    }

    private var mRadius = 0                 //轴承半径
    private var mItemOffset = 0             //item偏移量
    private var isBearingCanRoll = false    //轴承是否可以滚动
    private var isBearingOnBottom = false   //轴承是否在底部
    private var mCurrentBearingType = -1    //轴承类型
    private var mBearingColor = -1          //轴承颜色
    private var mBearingLayoutId = -1       //轴承布局id
    private var mBearingView: View? = null  //轴承view
    private var mPaint: Paint? = null
    private var isItemDirectionFixed = false//item是否保持垂直
    private var mBearingOffset = 0          //中心偏移量


    var mPivotX = 0f
    var mPivotY = 0f

    var mSlidingHelper: SlidingHelper? = null
    var mStartX = 0f
    var mStartY = 0f //上次的坐标
    var mTouchSlop = 0f  //触发滑动的最小距离
    var isBeingDragged = false  //手指在滑动中

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.FanLayout)
        isBearingCanRoll = typeArray.getBoolean(R.styleable.FanLayout_bearing_can_roll, false)
        isBearingOnBottom = typeArray.getBoolean(R.styleable.FanLayout_bearing_on_bottom, false)
        //轴承类型
        mCurrentBearingType = typeArray.getInteger(R.styleable.FanLayout_bearing_type, TYPE_COLOR)
        mBearingColor = typeArray.getColor(R.styleable.FanLayout_bearing_color, Color.BLACK)
        if (isViewType()) {
            //获取轴承布局
            mBearingLayoutId = typeArray.getResourceId(R.styleable.FanLayout_bearing_layout, 0)
            if (mBearingLayoutId == 0) {
                throw IllegalStateException("没有设置布局")
            } else {
                //加载布局
                mBearingView = LayoutInflater.from(context).inflate(mBearingLayoutId, this, false)
                addView(mBearingView)
            }
        } else {
            //如果是color类型 就获取轴承的半径 默认：0
            mRadius = typeArray.getDimensionPixelSize(R.styleable.FanLayout_bearing_radius, 0)
            //初始化画笔
            mPaint = Paint()
            mPaint?.isAntiAlias = true
            mPaint?.color = mBearingColor
            //调用onDraw
            setWillNotDraw(false)
        }
        mItemOffset = typeArray.getDimensionPixelSize(R.styleable.FanLayout_item_offset, 0)
        mCurrentGravity = typeArray.getInteger(R.styleable.FanLayout_bearing_gravity, LEFT)
        isItemDirectionFixed =
            typeArray.getBoolean(R.styleable.FanLayout_item_direction_fixed, false)
        mItemLayoutMode = typeArray.getInteger(
            R.styleable.FanLayout_item_layout_mode,
            MODE_AVERAGE)
        if (mItemLayoutMode == MODE_FIXED) {
            mItemAngleOffset = typeArray.getFloat(R.styleable.FanLayout_item_angle_offset, 0f)
            if (mItemAngleOffset <= 0 || mItemOffset > 360) {
                throw IllegalStateException("item_angle_offset must bt between 1~360")
            }
        }

        typeArray.recycle()

        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop.toFloat()
    }

    private fun isViewType(): Boolean {
        return mCurrentBearingType == TYPE_VIEW
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val isHasBottomBearing = isViewType() && isBearingOnBottom
        val startIndex = layoutBearing()
        layoutItems(isHasBottomBearing, startIndex)
    }

    private fun layoutBearing(): Int {
        var startIndex = 0
        if (isViewType()) {
            val width = (mBearingView?.measuredWidth ?: 0) / 2
            val height = (mBearingView?.measuredHeight ?: 0) / 2
            mBearingView?.layout(
                mPivotX.toInt() - width,
                mPivotY.toInt() - height,
                mPivotX.toInt() + width,
                mPivotY.toInt() + height
            )
            startIndex = 1
        }
        return startIndex
    }

    private fun layoutItems(isHasBottomBearing: Boolean, startIndex: Int) {
        val angle = 360f / (childCount - startIndex)
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view == mBearingView)
                return
            val layoutHeight = view.measuredHeight / 2
            val layoutWidth = view.measuredWidth

            if (mCurrentGravity == RIGHT || mCurrentGravity == RIGHT_TOP || mCurrentGravity == RIGHT_BOTTOM) {
                val baseLeft = mPivotX.toInt() - mRadius - mItemOffset
                view.layout(
                    baseLeft - width,
                    (mPivotY - layoutHeight).toInt(),
                    baseLeft,
                    (mPivotY + layoutHeight).toInt()
                )
                view.pivotX = (width + mRadius + mItemOffset).toFloat()
            } else {
                view.layout(
                    mPivotX.toInt() + mRadius + mItemOffset,
                    (mPivotY - layoutHeight).toInt(),
                    (mPivotX + layoutWidth).toInt(),
                    (mPivotY + layoutHeight).toInt()
                )
                //旋转点
                view.pivotX = (-mRadius - mItemOffset).toFloat()
            }
            view.pivotY = layoutHeight.toFloat()
            //如果view类型的轴承在底部的话， 还要减去1
            val index = if (isHasBottomBearing) i - 1 else i
            view.rotation = index * angle
        }
    }

    private fun updateCircleCenterPoint() {
        var cx = 0
        var cy = 0
        val totalHeight = measuredHeight
        val totalWidth = measuredWidth
        when (mCurrentGravity) {
            LEFT -> {
                cy = totalHeight / 2
                cx += mBearingOffset
            }

            RIGHT -> {
                cx = totalWidth
                cy = totalHeight / 2
                cx -= mBearingOffset
            }

            BOTTOM -> {
                cy = totalHeight
                cx = totalWidth / 2
                cy -= mBearingOffset
            }

            TOP -> {
                cx = totalWidth / 2
                cy += mBearingOffset
            }

            RIGHT_BOTTOM -> {
                cy = totalHeight - mBearingOffset
                cx = totalWidth - mBearingOffset
            }

            LEFT_BOTTOM -> {
                cy = totalHeight - mBearingOffset
                cx += mBearingOffset
            }

            RIGHT_TOP -> {
                cx = totalWidth - mBearingOffset
                cy += mBearingOffset
            }

            LEFT_TOP -> {
                cx = mBearingOffset
                cy = mBearingOffset
            }
        }
        mPivotX = cx.toFloat()
        mPivotY = cy.toFloat()
        if (mSlidingHelper != null) {
            mSlidingHelper?.updatePivotX(cx)
            mSlidingHelper?.updatePivotY(cy)
        }
    }

    public fun setGravity(@Gravity gravity: Int) {
        if (mCurrentGravity != gravity) {
            mCurrentGravity = gravity
            requestLayout()
        }
    }

    /**
     * 获取目标角度 (在屏幕能看见)
     */
    private fun getTargetAngle(): Int {
        var targetAngle = 0
        when (mCurrentGravity) {
            TOP -> {
                targetAngle = 90
            }

            BOTTOM -> {
                targetAngle = 270
            }

            LEFT_TOP, RIGHT_BOTTOM -> {
                targetAngle = 45
            }

            LEFT_BOTTOM, RIGHT_TOP -> {
                targetAngle = 315
            }

            LEFT, RIGHT -> {
                targetAngle = 0
            }
        }
        return targetAngle
    }

    /**
     * 找出最近的item
     * @param targetAngle 目标角度
     * @return 最近Item的Index
     */
    fun findClosesViewPos(targetAngle: Int): Int {
        val startIndex = if (isBearingOnBottom) 1 else 0
        var curRotation = getChildAt(startIndex).rotation
        if (targetAngle == 0 && curRotation > 180) {
            //如果是 左右  item>180 超过半圆 需要拿小的角度
            curRotation = 360 - curRotation
        }
        //离目标最近的角度
        var curMinRotation = abs(targetAngle - curRotation)
        //离目标最近的Item索引
        var curMinPos = startIndex
        //遍历子View
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView == mBearingView)
                continue
            //当前Item的旋转角度
            curRotation = childView.rotation
            //去更小的一边
            if (targetAngle == 0 && curRotation > 180) {
                curRotation = 360 - curRotation
            }
            //计算当前距离
            val rotate = abs(targetAngle - curRotation)
            if (rotate < curMinRotation) {
                curMinPos = i
                curMinRotation = rotation
            }
        }
        return curMinPos
    }

    /**
     * 滚动结束后,调整位置的动画
     */
    private fun playFixingAnimation() {
        if (isBeingDragged || childCount == 0 || (childCount == 1 && isViewType()))
            return
        var targetAngle = getTargetAngle()
        val index = findClosesViewPos(targetAngle)
        val rotate = getChildAt(index).rotation
        //判断要旋转的角度是否大于半圆 如果是的话 需要取另外一边的角
        if (abs(rotate - targetAngle) > 180) {
            targetAngle = 360 - targetAngle
        }
        val angle = abs(rotate - fixRotation(targetAngle))
    }

    /**
     * 调整角度 保持在0~360度内
     */
    private fun fixRotation(rotation: Int): Float {
        val angle = 360f
        var finalRo = 0f
        if (rotation < 0) {
            finalRo = angle + rotation
        }
        if (rotation > angle) {
            finalRo %= angle
        }
        return rotation.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val size: Int
        if (specMode == MeasureSpec.EXACTLY) {
            size = specSize
        } else {
            //获取最大的子View宽度
            var childMaxWidth = 0
            for (i in 0 until childCount) {
                childMaxWidth = Math.max(childMaxWidth, getChildAt(i).measuredWidth)
            }
            size = 2 * mRadius + mItemOffset + childMaxWidth
        }
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(
            size,
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) height else size
        )
        if (isViewType()) {
            mRadius =
                Math.max(mBearingView?.measuredWidth ?: 0, mBearingView?.measuredHeight ?: 0) / 2
        }
        updateCircleCenterPoint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mSlidingHelper == null) {
            mSlidingHelper = SlidingHelper.create(this, this)
            mSlidingHelper?.enableInertialSliding(true)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_MOVE && isBeingDragged || super.onInterceptTouchEvent(ev))
            return true
        if (!isEnabled)
            return false
        val x = ev.x
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mSlidingHelper?.abortAnimation()
                mStartX = x
                mStartY = y
                mSlidingHelper?.updateMovement(ev)
            }

            MotionEvent.ACTION_MOVE -> {
                val offsetX = x - mStartX
                val offsetY = y - mStartY
                if (abs(offsetX) > mTouchSlop || abs(offsetY) > mTouchSlop) {
                    mSlidingHelper?.updateMovement(ev)
                    isBeingDragged = true
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                isBeingDragged = false
            }
        }
        return isBeingDragged
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mSlidingHelper?.handleMovement(event)
        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_CANCEL -> {
                isBeingDragged = false
            }
        }
        return true
    }

    override fun addView(child: View?) {
        //如果是view类型轴承 && 设置顶部 需要再add完子
        var needAdd = false
        if (isViewType() && !isBearingOnBottom && childCount > 0 && child != mBearingView) {
            if (mBearingView != null) {
                super.removeView(mBearingView)
                needAdd = true
            }
        }
        super.addView(child)

        if (needAdd) {
            addView(mBearingView)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isViewType() && isBearingOnBottom) {
            mPaint?.let { canvas?.drawCircle(mPivotX, mPivotY, mRadius.toFloat(), it) }
        }
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        if (!isViewType() && !isBearingOnBottom) {
            mPaint?.let { canvas?.drawCircle(mPivotX, mPivotY, mRadius.toFloat(), it) }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mSlidingHelper != null) {
            mSlidingHelper?.release()
            mSlidingHelper = null
        }
    }

    override fun onSlideFinished() {
    }

    override fun onSliding(angle: Float) {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view == mBearingView && isViewType() && !isBearingCanRoll) {
                continue
            }
            view.rotation = view.rotation + angle
            Log.i("textetxtetxtetetette", view.rotation.toString())
            if (isItemDirectionFixed) {
                if (view != mBearingView && view is ViewGroup) {
                    val viewGroup = view as ViewGroup
                    for (j in 0 until viewGroup.childCount) {
                        val childView = viewGroup.getChildAt(j)
                        childView.rotation = -viewGroup.rotation
                    }
                }
            }
        }
    }
}