package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import com.ignotusvia.speechbuddy.core.VoiceRecorderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SpeechTranslationViewModel @Inject constructor(
    private val voiceRecorderManager: VoiceRecorderManager
) : ViewModel() {

    val voiceState: StateFlow<VoiceRecorderManager.RecordingState> get() = voiceRecorderManager.voiceState

    fun startRecording() {
        voiceRecorderManager.startRecording()
    }

    fun stopRecording() {
        voiceRecorderManager.stopRecording()
    }
}