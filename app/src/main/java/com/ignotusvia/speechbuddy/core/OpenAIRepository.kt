package com.ignotusvia.speechbuddy.core

import com.ignotusvia.speechbuddy.model.CompletionRequest
import com.ignotusvia.speechbuddy.remote.ChatGPTApi
import javax.inject.Inject

class OpenAIRepository @Inject constructor(
    private val chatGPTApi: ChatGPTApi
) {
    suspend fun getCompletion(prompt: String): String? {
        val request = CompletionRequest(prompt = prompt, maxTokens = 50, n = 1, stop = listOf("\n"))
        val response = chatGPTApi.getCompletion(request)
        return response.body()?.choices?.firstOrNull()?.text
    }
}
