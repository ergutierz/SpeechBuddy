package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.viewmodel.ConversationViewModel

@Composable
fun ConversationScreen() {
    val viewModel: ConversationViewModel = hiltViewModel()
    var currentMessage by remember { mutableStateOf(TextFieldValue("")) }
    val messages = remember { mutableStateListOf<String>() } // Example message list

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Interactive Conversations",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Messages display
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                Text(text = message, color = Color.Black)
            }
        }

        // Message input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                label = { Text("Type a message") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    // Send message and update conversation
                    messages.add(currentMessage.text)
                    currentMessage = TextFieldValue("") // Reset input field
                }
            ) {
                Text("Send")
            }
        }
    }
}
