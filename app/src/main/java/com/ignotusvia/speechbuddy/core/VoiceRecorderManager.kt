package com.ignotusvia.speechbuddy.core

import android.content.Context
import android.util.Log
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1.RecognitionAudio
import com.google.cloud.speech.v1.RecognitionConfig
import com.google.cloud.speech.v1.RecognizeRequest
import com.google.cloud.speech.v1.SpeechClient
import com.google.cloud.speech.v1.SpeechRecognitionAlternative
import com.google.cloud.speech.v1.SpeechRecognitionResult
import com.google.cloud.speech.v1.SpeechSettings
import com.google.protobuf.ByteString
import com.ignotusvia.speechbuddy.R
import com.ignotusvia.speechbuddy.di.ApplicationScope
import com.ignotusvia.speechbuddy.di.Dispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class VoiceRecorderManager @Inject constructor(
    @ApplicationScope applicationScope: CoroutineScope,
    @Dispatcher.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
    private val translationManager: TranslationManager
) : CoroutineScope by applicationScope {

    private var voiceRecorder: VoiceRecorder? = null
    private var speechClient: SpeechClient? = null
    private var byteArray: ByteArray = byteArrayOf()
    private val _voiceState = MutableStateFlow<RecordingState>(RecordingState())
    val voiceState: StateFlow<RecordingState> get() = _voiceState.asStateFlow()

    init {
        initializeSpeechClient()
    }

    fun startRecording(targetLanguageCode: String) {
        byteArray = byteArrayOf()
        voiceRecorder = VoiceRecorder(object : VoiceRecorder.Callback() {
            override fun onVoiceStart() {
                Log.d("VoiceRecorderManager", "onVoiceStart")
            }

            override fun onVoice(data: ByteArray?, size: Int) {
                Log.d("VoiceRecorderManager", "onVoice")
                byteArray = data?.let { byteArray.plus(it) }!!
                Log.e("kya", "***$byteArray")
                val rmsValue = calculateRMSValue(data)
                Log.d("VoiceRecorderManager", "rmsValue: $rmsValue")
                _voiceState.update { oldState ->
                    oldState.copy(data = byteArray, size = size, rmsValue = rmsValue)
                }
            }

            override fun onVoiceEnd() {
                Log.d("VoiceRecorderManager", "onVoiceEnd")
                Log.d("VoiceRecorderManager", "byteArray: $byteArray")
                transcribeRecording(byteArray, targetLanguageCode)
            }
        })
        voiceRecorder?.start()
    }

    fun stopRecording() {
        voiceRecorder?.stop()
        voiceRecorder = null
        _voiceState.update { it.copy(isTranscribing = true) }
    }

    private fun initializeSpeechClient() {
        val credentials =
            GoogleCredentials.fromStream(context.resources.openRawResource(R.raw.credentials))
        val credentialsProvider = FixedCredentialsProvider.create(credentials)
        speechClient = SpeechClient.create(
            SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build()
        )
    }

    private fun calculateRMSValue(buffer: ByteArray): Float {
        var sum = 0.0
        for (b in buffer) {
            val value = b.toInt()
            sum += (value * value).toDouble()
        }
        val average = sum / buffer.size
        return sqrt(average).toFloat()
    }


    private fun transcribeRecording(data: ByteArray, targetLanguageCode: String) {
        launch(ioDispatcher) {
            val response = speechClient?.recognize(createRecognizeRequestFromVoice(data))
            val results = response?.resultsList
            if (results != null) {
                val transcription = processTranscriptionResults(results)
                _voiceState.update { oldState ->
                    oldState.copy(
                        transcription = transcription,
                        isTranscribing = false
                    )
                }
                translateTranscription(transcription, targetLanguageCode)
            }
        }
    }

    private fun translateTranscription(transcription: String, targetLanguageCode: String) {
        translationManager.translateText(
            text = transcription,
            sourceLanguage = "en",
            targetLanguage = targetLanguageCode,
            onResult = { translatedText ->
                _voiceState.update { oldState -> oldState.copy(translatedText = translatedText) }
                launch(ioDispatcher) {
                    convertTextToAudio(translatedText)
                }
            },
            onError = { exception ->
                Log.d("VoiceRecorderManager", "Error translating text: $exception")
                _voiceState.update { oldState -> oldState.copy(translatedText = exception.message) }
            }
        )
    }

    private suspend fun convertTextToAudio(translatedText: String) = withContext(ioDispatcher) {
        val translatedAudioFileUri = translationManager.generateTranslationAudio(translatedText, "es")
        _voiceState.update { oldState ->
            oldState.copy(
                translatedAudioFileUri = translatedAudioFileUri
            )
        }
    }

    private fun processTranscriptionResults(results: List<SpeechRecognitionResult>): String {
        val stringBuilder = StringBuilder()
        for (result in results) {
            val recData: SpeechRecognitionAlternative = result.alternativesList[0]
            stringBuilder.append(recData.transcript)
        }
        return stringBuilder.toString()
    }


    private fun createRecognizeRequestFromVoice(audioData: ByteArray): RecognizeRequest {
        val audioBytes =
            RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(audioData)).build()
        val config = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setSampleRateHertz(16000)
            .setLanguageCode("en-US")
            .build()
        return RecognizeRequest.newBuilder()
            .setConfig(config)
            .setAudio(audioBytes)
            .build()
    }

    fun reset() {
        _voiceState.value = RecordingState()
    }

    data class RecordingState(
        val data: ByteArray? = null,
        val size: Int = 0,
        val transcription: String? = null,
        val translatedText: String? = null,
        val translatedAudioFileUri: String? = null,
        val rmsValue: Float = 0f,
        val isTranscribing: Boolean = false
    )
}
