package com.ignotusvia.speechbuddy.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommandManager
import com.ignotusvia.speechbuddy.view.screen.SpeechBuddyContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationCommandManager: NavigationCommandManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}