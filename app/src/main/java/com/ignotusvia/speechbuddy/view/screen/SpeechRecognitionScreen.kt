package com.ignotusvia.speechbuddy.view.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.core.VoiceRecorderManager
import com.ignotusvia.speechbuddy.viewmodel.SpeechRecognitionViewModel

@Composable
fun SpeechRecognitionScreen() {
    val viewModel: SpeechRecognitionViewModel = hiltViewModel()
    val viewState by viewModel.voiceState.collectAsState()
    val context = LocalContext.current
    var isGranted by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            isGranted = it
        }
    )

    LaunchedEffect(key1 = true) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            isGranted = true
        }
    }

    if (!isGranted) {
        PermissionRequiredUI()
    } else {
        MainContent(viewModel::startRecording, viewModel::stopRecording, viewState)
    }
}

@Composable
fun PermissionRequiredUI() {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Permission for recording is required to use this feature.")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Please grant the permission to proceed.")
    }
}

@Composable
fun MainContent(
    startRecording: () -> Unit,
    stopRecording: () -> Unit,
    viewState: VoiceRecorderManager.RecordingState
) {
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

        BasicTextField(
            value = recognizedText,
            onValueChange = { recognizedText = it },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                isRecording = !isRecording
                if (isRecording) {
                    startRecording()
                } else {
                    stopRecording()
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
                contentDescription = if (isRecording) "Stop Recording" else "Start Recording"
            )
            Text(text = if (isRecording) "Stop" else "Start")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Input",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = viewState.transcription.orEmpty(),
            style = MaterialTheme.typography.body1
        )

        Divider()

        Text(
            text = "Analysis",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = viewState.analysis.orEmpty(),
            style = MaterialTheme.typography.body1
        )
    }
}
