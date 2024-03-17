package com.ignotusvia.speechbuddy.core

sealed class Screen(val route: String) {
    data object SpeechTranslationScreen : Screen("speech_translation_screen")
    data object ConversationScreen : Screen("conversation_screen")
    data object LearningPathScreen : Screen("learning_path_screen")
    data object VocabularyExercisesScreen : Screen("vocabulary_exercises_screen")
    data object GrammarTipsScreen : Screen("grammar_tips_screen")
    data object ImmersionFeaturesScreen : Screen("immersion_features_screen")
    data object ProgressTrackingScreen : Screen("progress_tracking_screen")
    data object CommunityScreen : Screen("community_screen")
    data object AccessibilitySettingsScreen : Screen("accessibility_settings_screen")
    data object PrivacySettingsScreen : Screen("privacy_settings_screen")
    data object DashboardScreen : Screen("dashboard_screen")
    data object LoginScreen : Screen("login_screen")
    data object RegisterScreen : Screen("register_screen")
    data object ForgotPasswordScreen : Screen("forgot_password_screen")

    companion object {
        fun getScreenTitle(route: String?): String {
            return when (route) {
                SpeechTranslationScreen.route -> "Speech Translation"
                ConversationScreen.route -> "Interactive Conversations"
                LearningPathScreen.route -> "Learning Path"
                VocabularyExercisesScreen.route -> "Vocabulary Exercises"
                GrammarTipsScreen.route -> "Grammar Tips"
                ImmersionFeaturesScreen.route -> "Language Immersion"
                ProgressTrackingScreen.route -> "Progress Tracking"
                CommunityScreen.route -> "Community"
                AccessibilitySettingsScreen.route -> "Accessibility Settings"
                PrivacySettingsScreen.route -> "Privacy and Security"
                RegisterScreen.route -> "Register"
                LoginScreen.route -> "Login"
                ForgotPasswordScreen.route -> "Forgot Password"
                else -> "Speech Buddy"
            }
        }
    }
}
