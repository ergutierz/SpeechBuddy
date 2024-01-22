package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.viewmodel.ProgressTrackingViewModel

@Composable
fun ProgressTrackingScreen() {
    val viewModel: ProgressTrackingViewModel = hiltViewModel()
    // Example progress data
    val progressPercentage = remember { mutableStateOf(70) } // 70% for illustration
    val achievements = remember { mutableStateListOf("Completed 5 Lessons", "Perfect Score on Quiz") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Your Progress",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LinearProgressIndicator(
            progress = progressPercentage.value / 100f,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary
        )
        Text(
            text = "${progressPercentage.value}% Completed",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Achievements",
            style = MaterialTheme.typography.h6
        )
        achievements.forEach { achievement ->
            Text(text = achievement, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

