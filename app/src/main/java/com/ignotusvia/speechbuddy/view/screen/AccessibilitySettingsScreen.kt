package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.viewmodel.AccessibilitySettingsViewModel

@Composable
fun AccessibilitySettingsScreen() {
    val viewModel: AccessibilitySettingsViewModel = hiltViewModel()
    var textSize by remember { mutableStateOf(16f) } // Changed to Float
    var highContrast by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Accessibility Settings",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Text Size Adjustment
        Text("Adjust Text Size", style = MaterialTheme.typography.subtitle1)
        Slider(
            value = textSize,
            onValueChange = { textSize = it },
            valueRange = 12f..24f, // Changed to Float range
            modifier = Modifier.fillMaxWidth()
        )

        // High Contrast Mode Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("High Contrast Mode", style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = highContrast,
                onCheckedChange = { highContrast = it }
            )
        }
    }
}
