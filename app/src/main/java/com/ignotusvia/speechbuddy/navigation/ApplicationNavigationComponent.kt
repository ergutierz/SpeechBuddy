package com.ignotusvia.speechbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ignotusvia.speechbuddy.core.Screen
import com.ignotusvia.speechbuddy.view.screen.AccessibilitySettingsScreen
import com.ignotusvia.speechbuddy.view.screen.CommunityScreen
import com.ignotusvia.speechbuddy.view.screen.ConversationScreen
import com.ignotusvia.speechbuddy.view.screen.DashboardScreen
import com.ignotusvia.speechbuddy.view.screen.GrammarTipsScreen
import com.ignotusvia.speechbuddy.view.screen.ImmersionFeaturesScreen
import com.ignotusvia.speechbuddy.view.screen.LearningPathScreen
import com.ignotusvia.speechbuddy.view.screen.PrivacySettingsScreen
import com.ignotusvia.speechbuddy.view.screen.ProgressTrackingScreen
import com.ignotusvia.speechbuddy.view.screen.SpeechRecognitionScreen
import com.ignotusvia.speechbuddy.view.screen.VocabularyExercisesScreen

@Composable
fun ApplicationNavigationComponent(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.DashboardScreen.route
    ) {
        composable(Screen.DashboardScreen.route) { DashboardScreen() }
        composable(Screen.SpeechRecognitionScreen.route) { SpeechRecognitionScreen() }
        composable(Screen.ConversationScreen.route) { ConversationScreen() }
        composable(Screen.LearningPathScreen.route) { LearningPathScreen() }
        composable(Screen.VocabularyExercisesScreen.route) { VocabularyExercisesScreen() }
        composable(Screen.GrammarTipsScreen.route) { GrammarTipsScreen() }
        composable(Screen.ImmersionFeaturesScreen.route) { ImmersionFeaturesScreen() }
        composable(Screen.ProgressTrackingScreen.route) { ProgressTrackingScreen() }
        composable(Screen.CommunityScreen.route) { CommunityScreen() }
        composable(Screen.AccessibilitySettingsScreen.route) { AccessibilitySettingsScreen() }
        composable(Screen.PrivacySettingsScreen.route) { PrivacySettingsScreen() }
    }
}
