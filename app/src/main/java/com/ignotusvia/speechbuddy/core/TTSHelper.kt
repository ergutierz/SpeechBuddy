package com.ignotusvia.speechbuddy.core

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TTSHelper @Inject constructor(
    @ApplicationContext context: Context
) : TextToSpeech.OnInitListener {
    private val textToSpeech = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.language = Locale.getDefault()
        }
    }

    fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun shutDown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
