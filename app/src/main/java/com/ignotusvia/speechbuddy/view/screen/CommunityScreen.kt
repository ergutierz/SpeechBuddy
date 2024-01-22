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
import com.ignotusvia.speechbuddy.viewmodel.CommunityViewModel

@Composable
fun CommunityScreen() {
    val viewModel: CommunityViewModel = hiltViewModel()
    val communityPosts = remember { mutableStateListOf("User1: Hello everyone!", "User2: How's the learning going?") } // Example posts

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Community",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(communityPosts) { post ->
                CommunityPostItem(post)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Example input for new post
        var newPostText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = newPostText,
            onValueChange = { newPostText = it },
            label = { Text("Write a post") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // Add post to community (and clear input field after posting)
                communityPosts.add("You: $newPostText")
                newPostText = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Post")
        }
    }
}

@Composable
fun CommunityPostItem(post: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Text(
            text = post,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(16.dp)
        )
    }
}

