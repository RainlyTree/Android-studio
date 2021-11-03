package com.example.servicetest.viewPagerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servicetest.R

/**
 *  Created by chenlin on 2021/10/26.
 */
class MyAdapter : RecyclerView.Adapter<MyAdapter.PagerViewHolder>() {

    var mlist: List<Int> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pager, parent, false)
        return PagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bindData(mlist[position])
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    fun setlist(list: ArrayList<Int>) {
        mlist = list
    }


    class PagerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val mTv = itemView.findViewById<TextView>(R.id.item_pagerTv)

        fun bindData(i : Int) {
            mTv.text = i.toString()
        }
    }
}