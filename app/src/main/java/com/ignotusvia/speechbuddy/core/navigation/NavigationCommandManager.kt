package com.ignotusvia.speechbuddy.core.navigation

import androidx.navigation.NamedNavArgument
import com.ignotusvia.speechbuddy.core.Screen
import com.ignotusvia.speechbuddy.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationCommandManager @Inject constructor(
    @ApplicationScope private val coroutineScope: CoroutineScope,
) : CoroutineScope by coroutineScope {

    private val commandStore: MutableSharedFlow<NavigationCommand> = MutableSharedFlow(replay = 1)
    val commands: SharedFlow<NavigationCommand> = commandStore.asSharedFlow()

    fun navigate(directions: NavigationCommand) {
        launch { commandStore.emit(directions) }
    }

    fun logout() {
        launch { commandStore.emit(defaultDirection) }
    }

    companion object {
        val defaultDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = ""
        }

        val loginDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.LoginScreen.route
        }

        val registerDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.RegisterScreen.route
        }

        val dashboardDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.DashboardScreen.route
        }

        val speechRecognitionDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.SpeechTranslationScreen.route
        }

        val conversationDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.ConversationScreen.route
        }

        val learningPathDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.LearningPathScreen.route
        }

        val vocabularyExercisesDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.VocabularyExercisesScreen.route
        }

        val grammarTipsDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.GrammarTipsScreen.route
        }

        val immersionFeaturesDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.ImmersionFeaturesScreen.route
        }

        val progressTrackingDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.ProgressTrackingScreen.route
        }

        val communityDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.CommunityScreen.route
        }

        val accessibilitySettingsDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
            get() = Screen.AccessibilitySettingsScreen.route
        }

        val privacySettingsDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
            get() = emptyList()
            override val destination: String
            get() = Screen.PrivacySettingsScreen.route
        }
    }
}
