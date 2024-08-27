package com.example.servicetest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servicetest.R
import com.example.servicetest.helpUtils.ItemTouchHelperAdapter
import java.util.Collections

class MoveAdapter: RecyclerView.Adapter<MoveAdapter.MoveHolder>(), ItemTouchHelperAdapter {

    //数据
    var mData: ArrayList<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveHolder {
        return MoveHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_holder, parent, false))
    }

    override fun getItemCount(): Int {
        return mData?.count() ?: 0
    }

    override fun onBindViewHolder(holder: MoveHolder, position: Int) {
        holder.bind(mData?.get(position))
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        //交换位置
        Collections.swap(mData, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun oniTemDismiss(position: Int) {
        //移除数据
        mData?.removeAt(position)
        notifyItemRemoved(position)
    }

    class MoveHolder(view: View): RecyclerView.ViewHolder(view) {

        val textView = view.findViewById<TextView>(R.id.text)

        fun bind(mes: String?) {
            textView.text = mes
        }
    }
}