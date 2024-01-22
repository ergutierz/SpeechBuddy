package com.ignotusvia.speechbuddy.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ignotusvia.speechbuddy.view.MainActivity

class LogoutReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.ACTION_LOGOUT") {
            val loginIntent = Intent(context, MainActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(loginIntent)
        }
    }
}
