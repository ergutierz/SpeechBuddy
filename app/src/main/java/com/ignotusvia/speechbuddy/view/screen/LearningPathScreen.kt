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
import com.ignotusvia.speechbuddy.viewmodel.LearningPathViewModel

@Composable
fun LearningPathScreen() {
    val viewModel: LearningPathViewModel = hiltViewModel()
    val learningModules = remember { mutableStateListOf("Module 1", "Module 2", "Module 3") } // Example modules

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Your Learning Path",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(learningModules) { module ->
                LearningModuleItem(module)
            }
        }
    }
}

@Composable
fun LearningModuleItem(moduleName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = moduleName, style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { /* Navigate to module details */ }) {
                Text("Start")
            }
        }
    }
}

