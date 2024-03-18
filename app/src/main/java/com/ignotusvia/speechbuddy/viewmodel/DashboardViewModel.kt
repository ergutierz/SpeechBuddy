package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import com.ignotusvia.speechbuddy.R
import com.ignotusvia.speechbuddy.core.AlphabetManager
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommandManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.random.Random

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

    val colorResources = arrayOf(
        R.color.red_400,
        R.color.yellow_300,
        R.color.yellow_800,
        R.color.green_300,
        R.color.cyan_100,
        R.color.cyan_300,
        R.color.blue_200,
        R.color.blue_600,
        R.color.gray_500,
        R.color.gray_04,
        R.color.orange_500,
        R.color.transparent
    )

}
