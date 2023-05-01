package com.ignotusvia.speechbuddy.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChildSessionEntity::class], version = 1)
abstract class SpeechBuddyDB : RoomDatabase() {
    abstract fun childSessionDao(): ChildSessionDao
}