package com.example.servicetest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginRight
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  Created by chenlin on 2021/10/13.
 */
class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    var mList : ArrayList<String>? = null
    var mHeights: ArrayList<Int>? = null
    var mContext : Context? = null
    var mOnItemClickLister: OnItemClickLister? = null

    constructor(list: ArrayList<String>, context: Context) : this() {
        mList = list
        mContext = context
    }

    fun setHeight(list: ArrayList<Int>) {
        mHeights = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false)
        val myViewHolder = MyViewHolder(view)
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView?.text = mList?.get(position) ?: ""
        if(mOnItemClickLister != null) {
            holder.itemView.setOnClickListener(View.OnClickListener {
                mOnItemClickLister!!.OnItemClick(holder.itemView, position)
            })
            holder.itemView.setOnLongClickListener(View.OnLongClickListener {
                mOnItemClickLister!!.OnItemLongClick(holder.itemView, position)
                true
            })
        }

        mHeights?.let {
            val lp = holder.textView?.layoutParams
            lp?.height = it[position]
            holder.textView?.layoutParams = lp
        }
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    //删除其中的数据
    fun removeData(position: Int) {
        //删除之后位置会变
        mList?.removeAt(position)
//        notifyDataSetChanged()
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, mList?.size ?: 0 - 1)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = itemView.findViewById(R.id.tv_item)
    }

    interface OnItemClickLister {
        fun OnItemClick(view: View, position: Int)
        fun OnItemLongClick(view: View, position: Int)
    }

    fun setOnItemClickLister(OnItemClickLister: OnItemClickLister) {
        mOnItemClickLister = OnItemClickLister
    }

}

//枚举类
enum class orientationEnum(val orientation: Int) {
    HORIZONTAL_LIST(LinearLayoutManager.HORIZONTAL),
    VERTICAL_LIST(LinearLayoutManager.VERTICAL)
}


//每一个item的装饰类
class DividerItemDecoration() : RecyclerView.ItemDecoration() {

    companion object {
        val ARRTS: IntArray = IntArray(1) {
            android.R.attr.listDivider
        }

        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }

    var mDivider: Drawable? = null
    var mOrientation: Int? = null

    constructor(context: Context, orientation: orientationEnum): this() {
        mDivider = context.obtainStyledAttributes(ARRTS).getDrawable(0)
        setOrientation(orientation)
    }

    fun setOrientation(orientation: orientationEnum) {
        if(orientation.orientation != HORIZONTAL_LIST && orientation.orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        mOrientation = orientation.orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if(mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childcount = parent.childCount - 1
        for(i in 0..childcount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as LinearLayout.LayoutParams
            val left = child.right + child.marginRight
            val right = left + (mDivider?.intrinsicWidth ?: 0)
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount - 1
        for(i in 0..childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + (mDivider?.intrinsicHeight ?: 0)
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if(mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider?.intrinsicHeight ?: 0)
        } else {
            outRect.set(0, 0, mDivider?.intrinsicWidth ?: 0, 0)
        }
    }

}