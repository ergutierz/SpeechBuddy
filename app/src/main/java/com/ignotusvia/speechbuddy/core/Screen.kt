package com.ignotusvia.speechbuddy.core

sealed class Screen(val route: String) {
    object SpeechRecognitionScreen : Screen("speech_recognition_screen")
    object ConversationScreen : Screen("conversation_screen")
    object LearningPathScreen : Screen("learning_path_screen")
    object VocabularyExercisesScreen : Screen("vocabulary_exercises_screen")
    object GrammarTipsScreen : Screen("grammar_tips_screen")
    object ImmersionFeaturesScreen : Screen("immersion_features_screen")
    object ProgressTrackingScreen : Screen("progress_tracking_screen")
    object CommunityScreen : Screen("community_screen")
    object AccessibilitySettingsScreen : Screen("accessibility_settings_screen")
    object PrivacySettingsScreen : Screen("privacy_settings_screen")
    object DashboardScreen : Screen("dashboard_screen")
}
