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
import com.ignotusvia.speechbuddy.viewmodel.ImmersionFeaturesViewModel

@Composable
fun ImmersionFeaturesScreen() {
    val viewModel: ImmersionFeaturesViewModel = hiltViewModel()
    val immersionContent = remember { mutableStateListOf("Video Lesson 1", "Audio Exercise 2", "Article 3") } // Example content

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Language Immersion",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(immersionContent) { content ->
                ImmersionContentItem(content)
            }
        }
    }
}

@Composable
fun ImmersionContentItem(contentTitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = contentTitle, style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* Navigate to content details */ }) {
                Text("View")
            }
        }
    }
}

