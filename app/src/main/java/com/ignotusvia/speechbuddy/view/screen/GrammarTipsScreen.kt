package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.model.GrammarData
import com.ignotusvia.speechbuddy.model.GrammarSession
import com.ignotusvia.speechbuddy.model.Language
import com.ignotusvia.speechbuddy.viewmodel.GrammarTipsViewModel

@Composable
fun GrammarTipsScreen() {
    val viewModel: GrammarTipsViewModel = hiltViewModel()
    val languages by viewModel.availableLocales.collectAsState(emptyList())
    val targetLanguage by viewModel.targetLanguage.collectAsState()
    val grammarData by viewModel.grammarData.collectAsState(emptyList())

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            GrammarLanguageDropdown(
                languages = languages,
                targetLanguage = targetLanguage,
                onLanguageSelected = viewModel::setTargetLanguage
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Grammar and Usage Tips",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(grammarData) { topic ->
                GrammarTopicItem(topic)
            }
        }
    }
}

@Composable
fun GrammarTopicItem(grammar: GrammarSession) {
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
                Text(text = grammar.topic, style = MaterialTheme.typography.subtitle1)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
            if (expanded) {
                Row {
                    Column {
                        grammar.data.english.forEach { 
                            Text(text = it)
                        }
                    }
                    Column {
                        grammar.data.translation.forEach { 
                            Text(text = "-> $it")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GrammarLanguageDropdown(
    languages: List<Language>,
    targetLanguage: Language,
    onLanguageSelected: (Language) -> Unit
) {
    val expandedState = remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Source: English",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        Row {
            Text(
                text = "Target: ${targetLanguage.displayName}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
            IconButton(onClick = { expandedState.value = !expandedState.value }) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Toggle Dropdown"
                )
            }
        }

        DropdownMenu(
            expanded = expandedState.value,
            onDismissRequest = { expandedState.value = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    onClick = {
                        onLanguageSelected(language)
                        expandedState.value = false
                    }
                ) {
                    Text(text = language.displayName)
                }
            }
        }
    }
}

