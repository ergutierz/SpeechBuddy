package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ignotusvia.speechbuddy.core.AppCoroutineScope
import com.ignotusvia.speechbuddy.core.OpenAIRepository
import com.ignotusvia.speechbuddy.core.SpeechRecognitionHelper
import com.ignotusvia.speechbuddy.core.StateFlowModelStore
import com.ignotusvia.speechbuddy.core.TFLiteManager
import com.ignotusvia.speechbuddy.core.TTSHelper
import com.ignotusvia.speechbuddy.db.ChildSessionEntity
import com.ignotusvia.speechbuddy.db.SpeechBuddyDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tfliteManager: TFLiteManager,
    private val speechRecognitionHelper: SpeechRecognitionHelper,
    private val openAIRepository: OpenAIRepository,
    private val ttsHelper: TTSHelper,
    private val speechBuddyDB: SpeechBuddyDB,
    private val appCoroutineScope: AppCoroutineScope,
) : ViewModel() {

    private val modelStore = StateFlowModelStore(ViewState(), appCoroutineScope)

    val viewState: StateFlow<ViewState> = modelStore.modelState() as StateFlow<ViewState>

    fun onAction(action: Action) {
        when (action) {
            is Action.LoadModel -> loadModel("your_model.tflite")
            is Action.RunModel -> runModel()
            is Action.StartListening -> startListening()
            is Action.StopListening -> stopListening()
            is Action.GetImprovementSuggestion -> getImprovementSuggestion()
        }
    }

    private fun loadModel(modelName: String) {
        appCoroutineScope.launch {
            modelStore.process { it.copy(isLoading = true) }
            try {
                tfliteManager.loadModel(modelName)
                modelStore.process { it.copy(isLoading = false) }
            } catch (e: Exception) {
                modelStore.process { it.copy(isLoading = false, error = e) }
            }
        }
    }

    private fun runModel() {
        appCoroutineScope.launch {
            modelStore.process { it.copy(isLoading = true) }
            try {
                val inputTensor = tfliteManager.inputTensor
                val outputTensor = tfliteManager.outputTensor
                val inputArray = Array(1) { FloatArray(inputTensor.shape()[1]) }
                val outputArray = Array(1) { FloatArray(outputTensor.shape()[1]) }
                inputArray[0][0] = 1.0f
                inputArray[0][1] = 2.0f
                inputArray[0][2] = 3.0f
                tfliteManager.runModel(inputArray, outputArray)
                // Process the outputArray as needed and assign it to the result
                val result = processOutput(outputArray)
                modelStore.process { it.copy(result = result, isLoading = false) }
            } catch (e: Exception) {
                modelStore.process { it.copy(isLoading = false, error = e) }
            }
        }
    }

    private fun processOutput(outputArray: Array<FloatArray>): String {
        // Assuming outputArray[0] contains probabilities for different phonemes/words
        val probabilities = outputArray[0]

        // Find the index with the highest probability
        val mostProbableIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1

        // Convert the index to its corresponding phoneme/word
        val phonemesOrWords = listOf("Phoneme A", "Phoneme B", "Phoneme C") // Replace with actual phonemes/words
        val mostProbablePhonemeOrWord = phonemesOrWords.getOrElse(mostProbableIndex) { "Unknown" }

        return mostProbablePhonemeOrWord
    }



    private fun startListening() {
        speechRecognitionHelper.startListening()
    }

    private fun stopListening() {
        speechRecognitionHelper.stopListening()
    }

    private fun getImprovementSuggestion() {
        viewModelScope.launch {
            val recognizedText = viewState.value.result
            if (recognizedText != null) {
                val prompt = "Please provide suggestions for improving the pronunciation and language usage for the following speech: \"$recognizedText\""
                val suggestion = openAIRepository.getCompletion(prompt)

                if (suggestion != null) {
                    modelStore.process { it.copy(suggestion = suggestion) }
                    ttsHelper.speak(suggestion)
                    saveChildSession(recognizedText, suggestion)
                } else {
                    modelStore.process { it.copy(error = Exception("Failed to get improvement suggestions")) }
                }
            } else {
                modelStore.process { it.copy(error = Exception("No recognized speech available")) }
            }
        }
    }

    private suspend fun saveChildSession(recognizedSpeech: String, improvementSuggestion: String) {
        val childSessionEntity = ChildSessionEntity(0, System.currentTimeMillis(), recognizedSpeech, improvementSuggestion)
        speechBuddyDB.childSessionDao().insert(childSessionEntity)
    }

    private suspend fun analyzeChildSessions() {
        val childSessions = speechBuddyDB.childSessionDao().getAll()

        val patternsMap = mutableMapOf<String, MutableList<String>>()

        // Loop through each ChildSessionEntity and extract the recognized speech and improvement suggestion
        for (childSession in childSessions) {
            val recognizedSpeech = childSession.recognizedSpeech
            val improvementSuggestion = childSession.improvementSuggestion

            // Analyze the recognized speech to identify patterns
            val patterns = analyzeSpeech(recognizedSpeech)

            // Store the patterns and associated strengths/weaknesses in the data structure
            for (pattern in patterns) {
                if (patternsMap.containsKey(pattern)) {
                    val strengthsAndWeaknesses = patternsMap[pattern]!!
                    if (improvementSuggestion.contains("good job")) {
                        strengthsAndWeaknesses.add("strength")
                    } else {
                        strengthsAndWeaknesses.add("weakness")
                    }
                } else {
                    val strengthsAndWeaknesses = mutableListOf<String>()
                    if (improvementSuggestion.contains("good job")) {
                        strengthsAndWeaknesses.add("strength")
                    } else {
                        strengthsAndWeaknesses.add("weakness")
                    }
                    patternsMap[pattern] = strengthsAndWeaknesses
                }
            }
        }

        // Use the data structure to update the voice instructor's methodology accordingly
        updateVoiceInstructorMethodology(patternsMap)
    }

    private fun analyzeSpeech(speech: String): List<String> {
        // Split the speech into words
        val words = speech.split(" ")

        // Create an empty list to hold the patterns
        val patterns = mutableListOf<String>()

        // Loop through each word in the speech
        for (word in words) {
            // Remove any punctuation from the word
            val cleanWord = word.replace("[^A-Za-z0-9 ]".toRegex(), "")

            // If the word is longer than 3 characters and contains a vowel sound, add the first three characters to the patterns list
            if (cleanWord.length >= 3 && hasVowelSound(cleanWord)) {
                patterns.add(cleanWord.substring(0, 3))
            }
        }

        return patterns
    }

    private fun hasVowelSound(word: String): Boolean {
        // Check if the word contains a vowel sound (i.e. aeiouy)
        return word.matches(".*[aeiouyAEIOUY].*".toRegex())
    }


    private fun updateVoiceInstructorMethodology(patternsMap: Map<String, List<String>>) {
        // Create a list of all the patterns found in the child's speech
        val allPatterns = patternsMap.keys.toList()

        // Create an empty list to hold the recommendations
        val recommendations = mutableListOf<String>()

        // Loop through each pattern and analyze the strengths/weaknesses associated with it
        for (pattern in allPatterns) {
            val strengthsAndWeaknesses = patternsMap[pattern]!!
            val numStrengths = strengthsAndWeaknesses.count { it == "strength" }
            val numWeaknesses = strengthsAndWeaknesses.count { it == "weakness" }

            // Generate a recommendation based on the strengths/weaknesses associated with the pattern
            if (numWeaknesses > numStrengths) {
                recommendations.add("Work on improving pronunciation for words that contain the pattern \"$pattern\"")
            } else if (numStrengths > numWeaknesses) {
                recommendations.add("Encourage the child to use words that contain the pattern \"$pattern\" more often")
            }
        }

        // If there are no recommendations, add a default recommendation
        if (recommendations.isEmpty()) {
            recommendations.add("Continue with the current teaching methodology")
        }

        // Update the view state with the recommendations
        appCoroutineScope.launch {
            modelStore.process { it.copy(recommendations = recommendations) }
        }
    }


    override fun onCleared() {
        super.onCleared()
        ttsHelper.shutDown()
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val result: String? = null,
        val suggestion: String? = null,
        val recommendations: List<String> = emptyList(),
        val error: Throwable? = null,
        val sessionData: List<ChildSessionEntity>? = null
    )

    sealed class Action {
        data class RunModel(val inputArray: Array<FloatArray>) : Action()
        object LoadModel : Action()
        object StartListening : Action()
        object StopListening : Action()
        object GetImprovementSuggestion : Action()
    }

}