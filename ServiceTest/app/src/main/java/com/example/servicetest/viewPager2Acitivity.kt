package com.example.servicetest

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.servicetest.viewPagerAdapter.MyAdapter
import com.example.servicetest.viewPagerAdapter.adapterFragmentPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class viewPager2Acitivity : AppCompatActivity() {

    var list: ArrayList<Int>? = null

    var pager: ViewPager2? = null
    var tab_layout: TabLayout? = null

    var mediator: TabLayoutMediator? = null

    companion object {
        val tabs: Array<String> = arrayOf("排位赛", "匹配赛", "匹配赛")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager2_acitivity)

        pager = findViewById<ViewPager2>(R.id.view_pager2)
        tab_layout = findViewById<TabLayout>(R.id.my_tab_layout)

        pager?.offscreenPageLimit = 1
        pager?.adapter = adapterFragmentPager(this)
        pager?.registerOnPageChangeCallback(changeCallBack)

        mediator = TabLayoutMediator(tab_layout!!, pager!!) {
            tab, position ->
            val tabView = TextView(this)
            tabView.text = tabs[position]
            tabView.textSize = 14f
            tab.customView = tabView
            tab.view.setOnClickListener {
                pager?.currentItem = tab.position
            }
        }
        mediator?.attach()

        tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                pager?.currentItem = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    val changeCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val tabCount = tab_layout?.tabCount?.minus(1)
            for(i in 0..(tabCount ?: 0)) {
                val tab = tab_layout?.getTabAt(i)
                val tabView = tab?.customView as TextView
                if(tab.position == position) {
                    tabView.textSize = 20F
                    tabView.typeface = Typeface.DEFAULT
                } else {
                    tabView.textSize = 14F
                    tabView.typeface = Typeface.DEFAULT
                }
            }
        }

    }

    private fun setlist() {
        list = ArrayList()
        for(i in 1..10) {
            list?.add(i)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        pager?.registerOnPageChangeCallback(changeCallBack)
    }
}