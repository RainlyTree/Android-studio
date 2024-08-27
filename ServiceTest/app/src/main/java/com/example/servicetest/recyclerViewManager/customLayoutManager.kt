package com.example.servicetest.recyclerViewManager

import android.graphics.Path
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.servicetest.customLayoutManager.Keyframes
import com.example.servicetest.customLayoutManager.PosTan

/**
 * 实现路径Recycler
 */
class customLayoutManager(
    private var path: Path? = null,
    var mItemOffset: Int,
    @RecyclerView.Orientation var mOrientation: Int = RecyclerView.VERTICAL
) : RecyclerView.LayoutManager() {

    var mKeyframes: Keyframes? = null
    var mItemCountInScreen = 0  //同时最多显示的Item数量

    var mOffsetX = 0f
    var mOffsetY = 0f

    init {
        updatePath(path)
    }

    fun updatePath(path: Path?) {
        path?.let {
            mKeyframes = Keyframes(it)
            if (mItemOffset == 0) {
                //这里不允许间距为0 间距为0 所有Item都会叠在一起
                throw IllegalStateException("itemOffset must be > 0")
            }
            mItemCountInScreen = ((mKeyframes?.getPathLength()?.div(mItemOffset) ?: 0) + 1f).toInt()
            requestLayout()
        }
    }

    var mFirsyVisibleItemPos = 0
    public fun initNeedLayoutItems(result: ArrayList<PosTan>, itemCount: Int) {
        var currentDistance = 0f
        //获取第一个 最小的那个item
        for (i in 0 until itemCount) {
            currentDistance = i * mItemOffset - getScrollOffset()
            //>= 0 标识可见
            if (currentDistance >= 0) {
                mFirsyVisibleItemPos = i
                break
            }
        }
        //结束的position
        var endIndex = mFirsyVisibleItemPos + mItemCountInScreen
        if (endIndex > getItemCount()) {
            endIndex = getItemCount()
        }
        var fraction = 0f
        var posTan: PosTan?
        for (i in mFirsyVisibleItemPos until endIndex) {
            //得到当前距离
            currentDistance = i * mItemOffset - getScrollOffset()
            //得到百分比
            fraction = currentDistance / (mKeyframes?.getPathLength() ?: 0)
            //根据百分比去除对应坐标和角度
            posTan = mKeyframes?.getValue(fraction)
            posTan?.let {
                result.add(PosTan(posTan, i, fraction))
            }
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            //没有Item可以布局 回收全部临时缓存
            removeAndRecycleAllViews(recycler)
            return
        }
        //分离和回收全部有效的Item
        detachAndScrapAttachedViews(recycler)

        var needLayoutItems = arrayListOf<PosTan>()
        //获取需要布局的items
        initNeedLayoutItems(needLayoutItems, state.itemCount)
        //检查一下
        if (needLayoutItems.isEmpty() || mKeyframes == null) {
            removeAndRecycleAllViews(recycler)
            return
        }
        onLayout(recycler, needLayoutItems)
    }

    fun onLayout(recycler: Recycler, needLayoutItems: ArrayList<PosTan>) {
        var x = 0f
        var y = 0f
        var item : View
        needLayoutItems.forEachIndexed { index, pos ->
            //获取对应的view
            item = recycler.getViewForPosition(index)
            //添加进入
            addView(item)
            //测量item
            measureChild(item, 0, 0)

            //Path线条再View的中间
            x = pos.x - getDecoratedMeasuredWidth(item) / 2
            y = pos.y - getDecoratedMeasuredHeight(item) / 2

            //进行布局
            layoutDecorated(item, x.toInt(), y.toInt(), (x + getDecoratedMeasuredWidth(item)).toInt(),
                (y + getDecoratedMeasuredHeight(item)).toInt()
            )
            //旋转角度
            item.rotation = pos.angle
        }
    }

    fun getScrollOffset(): Float {
        return if (mOrientation == RecyclerView.VERTICAL) mOffsetY else mOffsetX
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        TODO("Not yet implemented")
    }

    override fun canScrollHorizontally(): Boolean {
        return mOrientation == RecyclerView.HORIZONTAL
    }

    override fun canScrollVertically(): Boolean {
        return mOrientation == RecyclerView.VERTICAL
    }
}