package com.ignotusvia.speechbuddy.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ignotusvia.speechbuddy.core.Screen
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommand
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommandManager
import com.ignotusvia.speechbuddy.navigation.ApplicationNavigationComponent
import kotlinx.coroutines.launch

@Composable
fun SpeechBuddyApp(
    modifier: Modifier = Modifier,
    navigationCommandManager: NavigationCommandManager
) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = getScreenTitle(currentRoute)) },
                navigationIcon = {
                    if (currentRoute != Screen.DashboardScreen.route) {
                        IconButton(onClick = { navController.navigate(Screen.DashboardScreen.route) }) {
                            Icon(Icons.Filled.Close, contentDescription = "Close")
                        }
                    } else {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                }
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

    val directions: NavigationCommand by navigationCommandManager.commands.collectAsState(
        initial = NavigationCommandManager.defaultDirection
    )
    with(directions) {
        if (destination.isNotEmpty()) {
            navController.navigate(destination)
            navigationCommandManager.clearNavigationCommand()
        }
    }
}

private fun getScreenTitle(route: String?): String {
    return when (route) {
        Screen.SpeechRecognitionScreen.route -> "Speech Recognition"
        Screen.ConversationScreen.route -> "Interactive Conversations"
        Screen.LearningPathScreen.route -> "Learning Path"
        Screen.VocabularyExercisesScreen.route -> "Vocabulary Exercises"
        Screen.GrammarTipsScreen.route -> "Grammar Tips"
        Screen.ImmersionFeaturesScreen.route -> "Language Immersion"
        Screen.ProgressTrackingScreen.route -> "Progress Tracking"
        Screen.CommunityScreen.route -> "Community"
        Screen.AccessibilitySettingsScreen.route -> "Accessibility Settings"
        Screen.PrivacySettingsScreen.route -> "Privacy and Security"
        else -> "Speech Buddy"
    }
}
