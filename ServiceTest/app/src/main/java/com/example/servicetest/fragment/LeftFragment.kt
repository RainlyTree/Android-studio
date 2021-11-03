package com.example.servicetest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.servicetest.FragmentTextActivity
import com.example.servicetest.R

/**
 *  Created by chenlin on 2021/10/18.
 */
class LeftFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.left_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val button = activity?.findViewById<Button>(R.id.button)
        button?.setOnClickListener() {
            val activity = activity as FragmentTextActivity
            activity.replaceFragment(RightFragment())
        }
    }
}

