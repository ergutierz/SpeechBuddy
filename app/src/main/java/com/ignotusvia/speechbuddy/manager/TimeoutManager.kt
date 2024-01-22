package com.ignotusvia.speechbuddy.manager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.ignotusvia.speechbuddy.core.LogoutTimerService
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommandManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeoutManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navigationCommandManager: NavigationCommandManager
) {
    fun startService() {
        val intent = Intent(context, LogoutTimerService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    fun stopService() {
        val intent = Intent(context, LogoutTimerService::class.java)
        context.stopService(intent)
    }
}
