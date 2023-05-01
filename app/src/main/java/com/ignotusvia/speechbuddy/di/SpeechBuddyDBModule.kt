package com.ignotusvia.speechbuddy.di

import android.content.Context
import androidx.room.Room
import com.ignotusvia.speechbuddy.db.SpeechBuddyDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SpeechBuddyDBModule {

    @Provides
    @Singleton
    fun providesCartRoomDatabase(
        @ApplicationContext context: Context
    ): SpeechBuddyDB {
        return Room.databaseBuilder(
            context,
            SpeechBuddyDB::class.java,
            "SpeechBuddyDB"
        ).build()
    }

}