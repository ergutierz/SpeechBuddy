package com.ignotusvia.speechbuddy.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommandManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationCommandManager: NavigationCommandManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeechBuddyApp(
                modifier = Modifier.fillMaxSize(),
                navigationCommandManager = navigationCommandManager
            )
        }
    }
}