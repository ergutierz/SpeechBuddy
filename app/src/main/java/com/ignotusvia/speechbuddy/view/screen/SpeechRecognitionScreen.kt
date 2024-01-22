package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.viewmodel.SpeechRecognitionViewModel

@Composable
fun SpeechRecognitionScreen() {
    val viewModel: SpeechRecognitionViewModel = hiltViewModel()
    var isRecording by remember { mutableStateOf(false) }
    var recognizedText by remember { mutableStateOf(TextFieldValue("")) }
    var analysisResult by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Speech Recognition",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = recognizedText,
            onValueChange = { recognizedText = it },
            label = { Text("Recognized Text") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                isRecording = !isRecording
                // Trigger start/stop speech recognition in viewModel
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = if (isRecording) "Stop Recording" else "Start Recording"
            )
            Text(text = if (isRecording) "Stop" else "Start")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Analysis",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = analysisResult,
            style = MaterialTheme.typography.body1
        )
    }
}
