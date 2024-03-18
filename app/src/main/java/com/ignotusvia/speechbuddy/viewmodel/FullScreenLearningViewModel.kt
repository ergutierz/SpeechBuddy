package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import android.content.Context
import android.speech.tts.TextToSpeech
import com.ignotusvia.speechbuddy.core.AlphabetManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FullScreenLearningViewModel @Inject constructor(
    private val alphabetManager: AlphabetManager,
    @ApplicationContext private val context: Context
) : ViewModel(), TextToSpeech.OnInitListener {

    private val _alphabet = MutableStateFlow<List<String>>(emptyList())
    val alphabet: StateFlow<List<String>> = _alphabet

    private val selectedLanguage = alphabetManager.selectedLanguage

    private lateinit var tts: TextToSpeech

    init {
        loadAlphabets()
        initializeTTS()
    }

    private fun initializeTTS() {
        tts = TextToSpeech(context, this)
    }

    fun speakText(text: String) {
        if (this::tts.isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (this::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }

    private fun loadAlphabets() {
        viewModelScope.launch {
            try {
                val json = context.assets.open("languages.json").bufferedReader().use { it.readText() }
                val jsonElement = Json.parseToJsonElement(json)
                val map = jsonElement.jsonObject.mapValues { (_, value) ->
                    value.jsonArray.map { it.jsonPrimitive.content }
                }
                selectedLanguage.value.let { languageCode ->
                    _alphabet.value = map[languageCode] ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val languages = tts.availableLanguages
            tts.language = Locale(selectedLanguage.value)
        }
    }
}
