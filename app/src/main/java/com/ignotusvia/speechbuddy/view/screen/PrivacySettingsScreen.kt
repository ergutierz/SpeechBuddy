package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.viewmodel.PrivacySettingsViewModel

@Composable
fun PrivacySettingsScreen() {
    val viewModel: PrivacySettingsViewModel = hiltViewModel()
    var isDataSharingEnabled by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Data Privacy and Security",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Data Sharing Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Data Sharing", style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isDataSharingEnabled,
                onCheckedChange = { isDataSharingEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Policy
        Text(
            text = "Read our Privacy Policy",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.clickable { /* Open privacy policy link */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Security Information
        Text(
            text = "Security Information",
            style = MaterialTheme.typography.h6
        )
        Text(
            text = "Your data is encrypted and stored securely. Learn more about our security practices.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.clickable { /* Open security practices link */ }
        )
    }
}

