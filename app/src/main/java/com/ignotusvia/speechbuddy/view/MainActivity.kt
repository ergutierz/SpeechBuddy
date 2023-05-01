package com.ignotusvia.speechbuddy.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ignotusvia.speechbuddy.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewState: MainViewModel.ViewState by viewModel.viewState.collectAsState()
            SpeechBuddyScreen(
                viewState = viewState,
                onAction = viewModel::onAction
            )
        }
    }
}