package com.sopt.peekabookaos.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sopt.peekabookaos.BuildConfig
import com.sopt.peekabookaos.data.source.local.LocalPrefDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RefreshRetrofitModule {
    private val json = Json { ignoreUnknownKeys = true }
    private const val CONTENT_TYPE = "Content-Type"
    private const val APPLICATION_JSON = "application/json"
    private const val BEARER = "Bearer "
    private const val ACCESS_TOKEN = "accessToken"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RefreshType

    @Provides
    @RefreshType
    fun providesRefreshInterceptor(
        localPrefDataSource: LocalPrefDataSource
    ): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(
                request
                    .newBuilder()
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .addHeader(ACCESS_TOKEN, BEARER + localPrefDataSource.accessToken)
                    .build()
            )
            response
        }

    @Provides
    @Singleton
    @RefreshType
    fun providesRefreshOkHttpClient(@RefreshType interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()

    @Provides
    @Singleton
    @RefreshType
    fun providesRefreshRetrofit(@RefreshType okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URI)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}