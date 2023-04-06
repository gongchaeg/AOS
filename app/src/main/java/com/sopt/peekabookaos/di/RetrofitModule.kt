package com.sopt.peekabookaos.di

import android.content.Context
import android.content.Intent
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sopt.peekabookaos.BuildConfig
import com.sopt.peekabookaos.data.source.local.LocalPrefDataSource
import com.sopt.peekabookaos.presentation.networkError.NetworkErrorActivity
import com.sopt.peekabookaos.util.extensions.isNetworkConnected
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private val json = Json { ignoreUnknownKeys = true }
    private const val CONTENT_TYPE = "Content-Type"
    private const val APPLICATION_JSON = "application/json"
    private const val BEARER = "Bearer "
    private const val ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjI2LCJpYXQiOjE2ODA2ODExNTIsImV4cCI6MTY4MTExMzE1Mn0.Oy1YfNsio6fHKguaLh1wly4f-QElc8G_FBF0CEFvrD0"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PeekaType

    @PeekaType
    @Provides
    fun providesPeekaInterceptor(
        @ApplicationContext context: Context,
        localPrefDataSource: LocalPrefDataSource
    ): Interceptor = Interceptor { chain ->
        if (!context.isNetworkConnected()) {
            context.startActivity(
                Intent(context, NetworkErrorActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            )
        }
        with(chain) {
            proceed(
                request()
                    .newBuilder()
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .addHeader(ACCESS_TOKEN, BEARER + localPrefDataSource.accessToken)
                    .build()
            )
        }
    }

    @PeekaType
    @Provides
    fun providesPeekaOkHttpClient(@PeekaType interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            ).build()

    @PeekaType
    @Provides
    fun providesPeekaRetrofit(@PeekaType okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URI)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}
