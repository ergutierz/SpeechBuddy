package com.ignotusvia.speechbuddy.model

data class GrammarData(
    val nouns: List<String> = emptyList(),
    val verbs: List<String> = emptyList(),
    val adjectives: List<String> = emptyList()
)

data class GrammarTranslation(val english: List<String>, val translation: List<String>)

data class GrammarSession(val topic: String, val data: GrammarTranslation)