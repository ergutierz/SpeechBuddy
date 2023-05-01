package com.ignotusvia.speechbuddy.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompletionRequest(
    val prompt: String,
    @Json(name="max_tokens")
    val maxTokens: Int,
    val n: Int,
    val stop: List<String>?
)

@JsonClass(generateAdapter = true)
data class CompletionResponse(
    val id: String,
    @Json(name="object")
    val objectData: String,
    val created: Long,
    val model: String,
    val usage: Usage,
    val choices: List<Choice>
)

@JsonClass(generateAdapter = true)
data class Usage(
    @Json(name="prompt_tokens")
    val promptTokens: Int,

    @Json(name="completion_tokens")
    val completionTokens: Int,

    @Json(name="total_tokens")
    val totalTokens: Int
    )

@JsonClass(generateAdapter = true)
data class Choice(val text: String)
