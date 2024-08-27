package com.example.servicetest.activity

import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servicetest.R
import com.example.servicetest.activity.baseActivity.BaseActivity
import com.example.servicetest.databinding.ActivityPathRecyclerviewBinding
import com.example.servicetest.recyclerViewManager.customLayoutManager
import org.w3c.dom.Text
import java.util.zip.Inflater

class PathRecyclerViewActivity : BaseActivity<ActivityPathRecyclerviewBinding>(){

    override fun initViewBinding(): ActivityPathRecyclerviewBinding {
        return ActivityPathRecyclerviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val path = Path()
        path.moveTo(250f, 250f)
        path.rLineTo(600f,300f);
        path.rLineTo(-600f,300f);
        path.rLineTo(600f,300f);
        path.rLineTo(-600f,300f);
        binding.rcyMes.adapter = PathAdapter()
        binding.rcyMes.layoutManager = customLayoutManager(path, 150)
    }

    class PathAdapter: RecyclerView.Adapter<PathAdapter.PathHolder>() {

        var mData: ArrayList<Int> = arrayListOf()

        init {
            for (i in 1..100) {
                mData.add(i)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PathHolder {
            return PathHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_path, parent, false))
        }

        override fun getItemCount(): Int {
            return mData.size
        }

        override fun onBindViewHolder(holder: PathHolder, position: Int) {
            holder.bind(mData[position])
        }

        class PathHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

            val mes = itemView.findViewById<TextView>(R.id.tv_mes)
            fun bind(cnt: Int) {
                mes.text = cnt.toString()
            }
        }
    }
}