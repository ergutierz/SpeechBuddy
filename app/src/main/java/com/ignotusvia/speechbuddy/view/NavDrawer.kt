package com.ignotusvia.speechbuddy.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ignotusvia.speechbuddy.core.Screen
import kotlinx.coroutines.launch

@Composable
fun NavDrawer(
    modifier: Modifier = Modifier,
    navController: NavController,
    scaffoldState: ScaffoldState
) {
    Column(modifier = modifier) {
        Text(text = "Speech Buddy", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
        Divider()

        // List of navigation items
        DrawerItem("Speech Translation", Screen.SpeechTranslationScreen.route, navController, scaffoldState)
        DrawerItem("Interactive Conversations", Screen.ConversationScreen.route, navController, scaffoldState)
        DrawerItem("Learning Path", Screen.LearningPathScreen.route, navController, scaffoldState)
        DrawerItem("Vocabulary Exercises", Screen.VocabularyExercisesScreen.route, navController, scaffoldState)
        DrawerItem("Grammar Tips", Screen.GrammarTipsScreen.route, navController, scaffoldState)
        DrawerItem("Language Immersion", Screen.ImmersionFeaturesScreen.route, navController, scaffoldState)
        DrawerItem("Progress Tracking", Screen.ProgressTrackingScreen.route, navController, scaffoldState)
        DrawerItem("Community", Screen.CommunityScreen.route, navController, scaffoldState)
        DrawerItem("Accessibility Settings", Screen.AccessibilitySettingsScreen.route, navController, scaffoldState)
        DrawerItem("Privacy and Security", Screen.PrivacySettingsScreen.route, navController, scaffoldState)
        DrawerItem("Logout", Screen.LoginScreen.route, navController, scaffoldState)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerItem(text: String, route: String, navController: NavController, scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()
    ListItem(
        text = { Text(text) },
        modifier = Modifier.clickable {
            coroutineScope.launch {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                scaffoldState.drawerState.close()
            }
        }
    )
}
