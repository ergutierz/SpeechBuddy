package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import com.ignotusvia.speechbuddy.core.AlphabetManager
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommandManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val commandManager: NavigationCommandManager,
    private val alphabetManager: AlphabetManager
) : ViewModel() {

    fun navigateToSpeechRecognition() {
        commandManager.navigate(NavigationCommandManager.speechRecognitionDirection)
    }

    fun navigateToVocabularyExercises() {
        commandManager.navigate(NavigationCommandManager.vocabularyExercisesDirection)
    }

    fun navigateToGrammarTips() {
        commandManager.navigate(NavigationCommandManager.grammarTipsDirection)
    }

    fun navigateToFullScreenLearning(languageCode: String) {
        alphabetManager.selectLanguage(languageCode)
        commandManager.navigate(NavigationCommandManager.fullScreenLearningDirection)
    }

    fun navigateToDashboard() {
        commandManager.navigate(NavigationCommandManager.dashboardDirection)
    }
}
