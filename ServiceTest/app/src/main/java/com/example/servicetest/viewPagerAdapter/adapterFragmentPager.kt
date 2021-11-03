package com.example.servicetest.viewPagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.servicetest.fragment.LeftFragment
import com.example.servicetest.fragment.RightFragment

/**
 *  Created by chenlin on 2021/10/26.
 */
class adapterFragmentPager(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        const val PAGE_RANK = 0
        const val PAGE_RANDOM = 1
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_RANK -> {
                RightFragment()
            }

            PAGE_RANDOM -> {
                RightFragment()
            }
            else -> RightFragment()
        }
    }
}