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
import com.ignotusvia.speechbuddy.viewmodel.VocabularyExercisesViewModel

@Composable
fun VocabularyExercisesScreen() {
    val viewModel: VocabularyExercisesViewModel = hiltViewModel()
    val vocabularyList = remember { mutableStateListOf("Word 1", "Word 2", "Word 3") } // Example words

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Vocabulary Building",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(vocabularyList) { word ->
                VocabularyItem(word)
            }
        }
    }
}

@Composable
fun VocabularyItem(word: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = word, style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* Show word details or quiz */ }) {
                Text("Learn More")
            }
        }
    }
}
