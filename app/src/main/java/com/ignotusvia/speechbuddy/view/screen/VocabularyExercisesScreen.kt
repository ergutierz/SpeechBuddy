package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.model.Vocabulary
import com.ignotusvia.speechbuddy.viewmodel.VocabularyExercisesViewModel

@Composable
fun VocabularyExercisesScreen() {
    val viewModel: VocabularyExercisesViewModel = hiltViewModel()
    val vocabularyData by viewModel.vocabularyData.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Vocabulary Building",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(vocabularyData) { vocabulary ->
                VocabularyItem(vocabulary)
            }
        }
    }
}

@Composable
fun VocabularyItem(vocabulary: Vocabulary) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = vocabulary.language.name, style = MaterialTheme.typography.subtitle1)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
            if (expanded) {
                if (vocabulary.isLoading) {
                    CircularProgressIndicator()
                } else {
                    vocabulary.vocabulary?.forEach { translation ->
                        Text(text = "${translation.englishWord} -> ${translation.translatedWord?: "Loading..."}")
                    }
                }
            }
        }
    }
}
