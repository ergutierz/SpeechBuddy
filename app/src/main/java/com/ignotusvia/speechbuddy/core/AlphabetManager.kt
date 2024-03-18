package com.ignotusvia.speechbuddy.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlphabetManager @Inject constructor() {
    private val _selectedLanguage = MutableStateFlow("")
    val selectedLanguage: StateFlow<String> get() = _selectedLanguage.asStateFlow()

    fun selectLanguage(languageCode: String) {
        _selectedLanguage.value = languageCode
    }
}