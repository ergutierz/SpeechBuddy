package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import com.ignotusvia.speechbuddy.core.VoiceRecorderManager
import com.ignotusvia.speechbuddy.model.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SpeechTranslationViewModel @Inject constructor(
    private val voiceRecorderManager: VoiceRecorderManager
) : ViewModel() {

    val voiceState: StateFlow<VoiceRecorderManager.RecordingState> get() = voiceRecorderManager.voiceState

    val availableLocales: Flow<List<Language>>
        get() {
            val availableLocales: Array<Locale> = Locale.getAvailableLocales()
            val supportedLanguages = mutableSetOf<Language>()

            for (locale in availableLocales) {
                val languageCode = locale.language
                val displayName = locale.displayLanguage
                if (languageCode.isNotEmpty()) {
                   supportedLanguages.add(Language(displayName, languageCode))
                }
            }
            return flowOf(supportedLanguages.toList().distinct())
        }

    val targetLanguage = MutableStateFlow(Language("es", "Spanish"))

    fun startRecording() {
        voiceRecorderManager.startRecording()
    }

    fun stopRecording() {
        voiceRecorderManager.stopRecording()
    }

    fun setTargetLanguage(language: Language) {
        targetLanguage.value = language
    }
}