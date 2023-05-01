package com.ignotusvia.speechbuddy.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_sessions")
data class ChildSession(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val timestamp: Long,
    val recognizedSpeech: String,
    val improvementSuggestion: String
)
