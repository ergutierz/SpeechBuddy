package com.ignotusvia.speechbuddy.core

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class TranslationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val TAG = "TranslationManager"
    private val textToSpeech: TextToSpeech = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            Log.d(TAG, "TextToSpeech initialized successfully.")
        } else {
            Log.e(TAG, "Error initializing TextToSpeech.")
        }
    }

    fun translateText(text: String, sourceLanguage: String, targetLanguage: String, onResult: (String) -> Unit, onError: (Exception) -> Unit) {
        Log.d(TAG, "Starting text translation.")
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(sourceLanguage))
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(targetLanguage))
            .build()

        val translator = Translation.getClient(options)

        translator.downloadModelIfNeeded().addOnSuccessListener {
            Log.d(TAG, "Model downloaded successfully.")
            translator.translate(text)
                .addOnSuccessListener { translatedText ->
                    Log.d(TAG, "Text translated successfully.")
                    onResult(translatedText)
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error translating text: $exception")
                    onError(exception)
                }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error downloading model: $exception")
            onError(exception)
        }
    }

    suspend fun generateTranslationAudio(text: String, targetLanguage: String): String = suspendCancellableCoroutine { cont ->
        textToSpeech.language = Locale.forLanguageTag(targetLanguage)
        textToSpeech.setSpeechRate(1.0f)
        textToSpeech.setPitch(1.0f)

        val utteranceId = UUID.randomUUID().toString()
        val tempFileName = "$utteranceId.mp3"
        val tempFile = File(context.cacheDir, tempFileName)

        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.d(TAG, "Starting audio synthesis for utterance ID: $utteranceId")
            }

            override fun onDone(utteranceId: String?) {
                Log.d(TAG, "Audio synthesis done for utterance ID: $utteranceId. Copying to MediaStore.")
                try {
                    val uriString = copyToMediaStore(tempFile, tempFileName)
                    cont.resume(uriString)
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                } finally {
                    tempFile.delete()
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                Log.e(TAG, "Error during audio synthesis for utterance ID: $utteranceId.")
                cont.resumeWithException(IllegalStateException("Error generating audio file for utterance ID: $utteranceId"))
            }
        })

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }
        textToSpeech.synthesizeToFile(text, params, tempFile, utteranceId)
    }

    private fun copyToMediaStore(tempFile: File, fileName: String): String {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC)
        }

        val uri = context.contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            context.contentResolver.openOutputStream(it).use { outputStream ->
                tempFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream!!)
                }
            }
            Log.d(TAG, "Audio file copied to MediaStore at URI: $uri")
            return uri.toString()
        } ?: run {
            throw IllegalStateException("Failed to insert audio into MediaStore.")
        }
    }
}
