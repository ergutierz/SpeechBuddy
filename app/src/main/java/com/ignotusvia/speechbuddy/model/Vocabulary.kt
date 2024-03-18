package com.ignotusvia.speechbuddy.model

import kotlinx.serialization.Serializable

@Serializable
data class VocabularyData(
    val words: List<String>
)

data class Vocabulary(
    val language: LanguageType,
    val vocabulary: List<VocabTranslation>? = null,
    val isLoading: Boolean = false
)

data class VocabTranslation(
    val englishWord: String,
    val translatedWord: String?
)

enum class LanguageType(val code: String) {
    FRENCH("fr"),
    SPANISH("es"),
    GERMAN("de"),
    RUSSIAN("ru"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KOREAN("ko"),
    CHINESE("zh"),
    ARABIC("ar"),
    HINDI("hi"),
    PORTUGUESE("pt"),
    DUTCH("nl"),
    TURKISH("tr"),
    POLISH("pl"),
    SWEDISH("sv"),
    DANISH("da"),
    ENGLISH("en")
}
