package com.ignotusvia.speechbuddy.remote

import com.ignotusvia.speechbuddy.model.CompletionRequest
import com.ignotusvia.speechbuddy.model.CompletionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatGPTApi {
    @POST("v1/engines/davinci-codex/completions")
    suspend fun getCompletion(@Body completionRequest: CompletionRequest): Response<CompletionResponse>
}
