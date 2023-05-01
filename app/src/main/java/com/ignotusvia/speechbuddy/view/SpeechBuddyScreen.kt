package com.ignotusvia.speechbuddy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ignotusvia.speechbuddy.R
import com.ignotusvia.speechbuddy.viewmodel.MainViewModel

@Composable
fun SpeechBuddyScreen(
    viewState: MainViewModel.ViewState,
    onAction: (action: MainViewModel.Action) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Speech Buddy",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Press the button and start speaking!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        val isListening = remember { mutableStateOf(false) }

        FloatingActionButton(
            modifier = Modifier
                .background(
                    color = if (isListening.value) colorResource(id = R.color.blue_100)
                    else colorResource(id = R.color.white)
                ),
            onClick = {
                if (isListening.value) {
                    onAction(MainViewModel.Action.StopListening)
                } else {
                    onAction(MainViewModel.Action.StartListening)
                }
                isListening.value = !isListening.value
            }
        ) {
            Icon(
                if (isListening.value) Icons.Default.AddHome else Icons.Default.Add,
                contentDescription = if (isListening.value) "Stop" else "Start"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (viewState.result != null) {
            Text(text = "Recognized Speech:", fontWeight = FontWeight.Bold)
            Text(text = viewState.result)
        }

        if (viewState.suggestion != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Improvement Suggestion:", fontWeight = FontWeight.Bold)
            Text(text = viewState.suggestion)
        }

        if (viewState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}
