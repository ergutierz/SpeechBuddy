package com.ignotusvia.speechbuddy.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ForApplication

@Module
@InstallIn(SingletonComponent::class)
object AndroidModule {

    @Provides
    @Singleton
    fun provideNetworkConnectivityManager(@ApplicationContext context: Context): ConnectivityManager? {
        return context.getSystemService()
    }

    @Provides
    @Singleton
    @ForApplication
    fun provideApplicationContext(application: Application): Context {
        return application
    }
}