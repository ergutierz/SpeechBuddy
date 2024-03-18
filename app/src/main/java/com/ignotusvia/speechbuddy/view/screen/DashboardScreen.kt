package com.ignotusvia.speechbuddy.view.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.model.Language
import com.ignotusvia.speechbuddy.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = hiltViewModel()
    var progress by remember { mutableStateOf(75) }
    val context = LocalContext.current
    BackHandler {
        (context as? Activity)?.finish()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Overall Progress: 75%",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow {
            item {
                QuickAccessItem(
                    "Speech Translation",
                    onClick = {
                        viewModel.navigateToSpeechRecognition()
                    })
                QuickAccessItem(
                    "Vocabulary Exercises",
                    onClick = {
                        viewModel.navigateToVocabularyExercises()
                    })
                QuickAccessItem(
                    "Grammar Tips",
                    onClick = {
                        viewModel.navigateToGrammarTips()
                    })
            }
        }

        Text(
            text = "Recommended for You",
            style = MaterialTheme.typography.h6
        )
        LazyRow(contentPadding = PaddingValues(8.dp)) {
            items(modules) { module ->
                LearningModule(moduleName = module.displayName){
                    viewModel.navigateToFullScreenLearning(module.languageCode)
                }
            }
        }
    }
}

val modules = listOf(
    Language("English", "en"),
    Language("Spanish", "es"),
    Language("French", "fr"),
    Language("German", "de"),
    Language("Italian", "it"),
    Language("Japanese", "ja"),
    Language("Korean", "ko"),
    Language("Portuguese", "pt"),
    Language("Russian", "ru"),
    Language("Chinese", "zh"),
    Language("Arabic", "ar"),
    Language("Greek", "el")
)

@Composable
fun LearningModule(moduleName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = moduleName, style = MaterialTheme.typography.subtitle1)

            }
        }
    }
}

@Composable
fun QuickAccessItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Composable
fun RecommendedItem(title: String) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.subtitle1)
        }
    }
}