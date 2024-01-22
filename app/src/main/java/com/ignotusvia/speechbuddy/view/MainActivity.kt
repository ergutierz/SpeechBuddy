package com.ignotusvia.speechbuddy.view

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ignotusvia.speechbuddy.core.LogoutReceiver
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommandManager
import com.ignotusvia.speechbuddy.manager.TimeoutManager
import com.ignotusvia.speechbuddy.view.screen.SpeechBuddyContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationCommandManager: NavigationCommandManager

    @Inject
    lateinit var timeoutManager: TimeoutManager

    private lateinit var logoutReceiver: LogoutReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLogoutReceiver()
        setContent {
            val navController = rememberNavController()
            SpeechBuddyContainer(
                modifier = Modifier.fillMaxSize(),
                navController = navController
            )

            LaunchedEffect("navigation") {
                navigationCommandManager.commands.onEach { navigationCommand ->
                    if (navigationCommand.destination.isNotEmpty()) {
                        navController.navigate(navigationCommand.destination)
                    }
                }.launchIn(this)
            }
        }
    }

    private fun registerLogoutReceiver() {
        logoutReceiver = LogoutReceiver()


        val filter = IntentFilter("com.example.ACTION_LOGOUT")
        registerReceiver(logoutReceiver, filter)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        timeoutManager.startService()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(logoutReceiver)
    }

}