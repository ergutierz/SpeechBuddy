package com.ignotusvia.speechbuddy.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.ignotusvia.speechbuddy.model.Language
import com.ignotusvia.speechbuddy.model.LanguageType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class GrammarTipsViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    private val _grammarData = MutableStateFlow(GrammarData(emptyList(), emptyList(), emptyList()))
    val grammarData: StateFlow<GrammarData> = _grammarData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val targetLanguage = MutableStateFlow(LanguageType.ENGLISH)  // Default language
    private var englishWords = GrammarData(emptyList(), emptyList(), emptyList())

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

    init {
        loadGrammarData()
        observeLanguageChanges()
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            targetLanguage.collectLatest { language ->
                if (englishWords.nouns.isNotEmpty()) {
                    translateGrammarData(language)
                }
            }
        }
    }

    private fun loadGrammarData() {
        viewModelScope.launch {
            _isLoading.value = true
            englishWords = GrammarData(
                nouns = loadJsonData("nouns.json"),
                verbs = loadJsonData("verbs.json"),
                adjectives = loadJsonData("adjectives.json")
            )
            _grammarData.value = englishWords
            _isLoading.value = false
        }
    }

    private fun loadJsonData(fileName: String): List<String> {
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val jsonObject = Json.decodeFromString<JsonObject>(json)
        return jsonObject["words"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
    }

    private suspend fun translateGrammarData(language: LanguageType) {
        viewModelScope.launch {
            _isLoading.value = true
            val translations = translateWords(englishWords.flatten(), language.code)
            val (translatedNouns, translatedVerbs, translatedAdjectives) = translations.divideByCategory(englishWords)

            _grammarData.value = GrammarData(
                nouns = translatedNouns,
                verbs = translatedVerbs,
                adjectives = translatedAdjectives
            )
            _isLoading.value = false
        }
    }

    private suspend fun translateWords(words: List<String>, targetLanguage: String): List<String> {
        val translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLanguage)
            .build()
        val englishTranslator = Translation.getClient(translatorOptions)

        return words.map { word ->
            translateWord(englishTranslator, word)
        }
    }

    private suspend fun translateWord(translator: Translator, word: String): String = suspendCancellableCoroutine { continuation ->
        translator.translate(word)
            .addOnSuccessListener { translatedText ->
                continuation.resume(translatedText)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    private fun List<String>.divideByCategory(original: GrammarData): Triple<List<String>, List<String>, List<String>> {
        val nouns = take(original.nouns.size)
        val verbs = drop(original.nouns.size).take(original.verbs.size)
        val adjectives = drop(original.nouns.size + original.verbs.size)
        return Triple(nouns, verbs, adjectives)
    }

    private fun GrammarData.flatten(): List<String> = nouns + verbs + adjectives
}


data class GrammarData(
    val nouns: List<String> = emptyList(),
    val verbs: List<String> = emptyList(),
    val adjectives: List<String> = emptyList()
)
