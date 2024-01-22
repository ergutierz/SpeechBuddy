package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.viewmodel.GrammarTipsViewModel

@Composable
fun GrammarTipsScreen() {
    val viewModel: GrammarTipsViewModel = hiltViewModel()
    val grammarTopics = remember { mutableStateListOf("Nouns", "Verbs", "Adjectives") } // Example topics

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Grammar and Usage Tips",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(grammarTopics) { topic ->
                GrammarTopicItem(topic)
            }
        }
    }
}

@Composable
fun GrammarTopicItem(topic: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = topic, style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* Navigate to detailed grammar content */ }) {
                Text("Explore")
            }
        }
    }
}

