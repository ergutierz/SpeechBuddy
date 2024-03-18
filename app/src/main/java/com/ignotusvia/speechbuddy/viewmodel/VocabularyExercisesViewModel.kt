package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import dagger.hilt.android.lifecycle.HiltViewModel
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.ignotusvia.speechbuddy.model.LanguageType
import com.ignotusvia.speechbuddy.model.VocabTranslation
import com.ignotusvia.speechbuddy.model.Vocabulary
import com.ignotusvia.speechbuddy.model.VocabularyData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@HiltViewModel
class VocabularyExercisesViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _vocabularyData = MutableStateFlow<List<Vocabulary>>(emptyList())
    val vocabularyData: StateFlow<List<Vocabulary>> = _vocabularyData.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val englishWords = extractEnglishWords()
            initializeVocabularyData(englishWords)
            loadTranslations(englishWords)
        }
    }

    private fun initializeVocabularyData(englishWords: List<String>) {
        val initialData = LanguageType.entries.map { language ->
            Vocabulary(language, englishWords.map { VocabTranslation(it, null) }, isLoading = true)
        }
        _vocabularyData.value = initialData
    }

    private suspend fun loadTranslations(englishWords: List<String>) {
        LanguageType.entries.forEach { languageType ->
            coroutineScope {
                val translationJobs = englishWords.map { word ->
                    async {
                        translateWord(word, languageType.code)
                    }
                }

                val translatedWords = translationJobs.awaitAll()

                // Combine the English words with their translations
                val translations = englishWords.zip(translatedWords).map { (english, translated) ->
                    VocabTranslation(english, translated)
                }

                // Update the vocabulary data with the new translations
                _vocabularyData.update { vocabularies ->
                    vocabularies.map { vocabulary ->
                        if (vocabulary.language == languageType) {
                            vocabulary.copy(vocabulary = translations, isLoading = false)
                        } else {
                            vocabulary
                        }
                    }
                }
            }
        }
    }

    private fun extractEnglishWords(): List<String> {
        val json = context.assets.open("englishvocabulary.json").bufferedReader().use { it.readText() }
        val vocabularyData = Json.decodeFromString<VocabularyData>(json)
        return vocabularyData.words
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun translateWord(word: String, targetLanguage: String): String = coroutineScope {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLanguage)
            .build()
        val translator = Translation.getClient(options)

        suspendCancellableCoroutine { continuation ->
            translator.downloadModelIfNeeded().addOnSuccessListener {
                translator.translate(word).addOnSuccessListener { translatedText ->
                    continuation.resume(translatedText) {}
                }.addOnFailureListener { continuation.cancel(it) }
            }.addOnFailureListener { continuation.cancel(it) }
        }
    }

}

