package com.ignotusvia.speechbuddy.view.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
            itemsIndexed(modules) { index, module ->
                val color = viewModel.colorResources[index]
                val colorInt = colorResource(id = color)
                LearningModule(moduleName = module.displayName, onClick = {
                    viewModel.navigateToFullScreenLearning(module.languageCode)
                }, color = colorInt)
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
fun LearningModule(moduleName: String, onClick: () -> Unit, color: Color) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        elevation = 4.dp,
        color = color
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = moduleName,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface
            )
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