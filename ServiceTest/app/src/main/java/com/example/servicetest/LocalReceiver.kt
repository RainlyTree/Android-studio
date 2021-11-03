package com.example.servicetest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 *  Created by chenlin on 2021/10/9.
 */
class LocalReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "This is in LocalReceiver", Toast.LENGTH_SHORT).show()
    }
}