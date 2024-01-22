package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ignotusvia.speechbuddy.navigation.ApplicationNavigationComponent
import com.ignotusvia.speechbuddy.view.NavDrawer
import com.ignotusvia.speechbuddy.view.TopNavBar

@Composable
fun SpeechBuddyContainer(modifier: Modifier = Modifier, navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = {
            TopNavBar(
                navController = navController,
                scaffoldState = scaffoldState
            )
        },
        drawerContent = {
            NavDrawer(
                navController = navController,
                scaffoldState = scaffoldState
            )
        },
        content = { padding ->
            ApplicationNavigationComponent(
                modifier = Modifier
                    .padding(padding),
                navController = navController
            )
        }
    )
}
