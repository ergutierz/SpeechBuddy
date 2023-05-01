package com.ignotusvia.speechbuddy.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_sessions")
data class ChildSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val recognizedSpeech: String,
    val improvementSuggestion: String
)
