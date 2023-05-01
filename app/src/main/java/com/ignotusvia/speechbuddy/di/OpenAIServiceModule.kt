package com.ignotusvia.speechbuddy.di

import com.ignotusvia.speechbuddy.BuildConfig.OPEN_AI_SERVICE_BASE_URL
import com.ignotusvia.speechbuddy.BuildConfig.OPEN_API_KEY
import com.ignotusvia.speechbuddy.core.NetworkConnectivityInterceptor
import com.ignotusvia.speechbuddy.core.NetworkConnectivityManager
import com.ignotusvia.speechbuddy.remote.ChatGPTApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OpenAPIBaseUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OpenApiClient

@Module
@InstallIn(SingletonComponent::class)
object OpenAIServiceModule {

    @Provides
    @Singleton
    fun provideOpenApiClient(
        @OpenAPIBaseUrl baseUrl: HttpUrl,
        @OpenApiClient okHttpClient: OkHttpClient
    ) : ChatGPTApi {
        return baseRetrofit()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
            .create(ChatGPTApi::class.java)
    }

    @Provides
    @Singleton
    @OpenApiClient
    fun provideOpenApiHttpClient(
        networkConnectivityManager: NetworkConnectivityManager,
        @LoggingInterceptor loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(NetworkConnectivityInterceptor(networkConnectivityManager))
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $OPEN_API_KEY")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    @LoggingInterceptor
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @OpenAPIBaseUrl
    fun provideOpenAPIBaseUrl(): HttpUrl {
        return OPEN_AI_SERVICE_BASE_URL.toHttpUrl()
    }

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private fun baseRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
    }

}