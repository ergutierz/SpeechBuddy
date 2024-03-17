package com.ignotusvia.speechbuddy.view.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = hiltViewModel()
    var progress by remember { mutableStateOf(50) }
    val context = LocalContext.current
    BackHandler {
        (context as? Activity)?.finish()
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Overview
        LinearProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Overall Progress: $progress%",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Quick Access to Main Features
        LazyRow {
            item {
                QuickAccessItem(
                    "Speech Recognition",
                    onClick = { /* Navigate to Speech Recognition */ })
                QuickAccessItem(
                    "Vocabulary Exercises",
                    onClick = { /* Navigate to Vocabulary Exercises */ })
                QuickAccessItem("Grammar Tips", onClick = { /* Navigate to Grammar Tips */ })
                // Add more items as needed
            }
        }

        // Recommended Lessons or Activities
        Text(
            text = "Recommended for You",
            style = MaterialTheme.typography.h6
        )
        LazyRow {
            item {
                // Populate with dynamic content
                RecommendedItem("Lesson 1")
                RecommendedItem("Activity 2")
                // Add more items based on user progress or preferences
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