package com.aseelsh.ytdexp.di

import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.aseelsh.ytdexp.data.db.AppDatabase
import com.aseelsh.ytdexp.data.repository.DownloadRepositoryImpl
import com.aseelsh.ytdexp.domain.repository.DownloadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val TIMEOUT_SECONDS = 30L

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ytdexp.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDownloadDao(db: AppDatabase) = db.downloadDao()

    @Provides
    @Singleton
    fun provideDownloadRepository(
        impl: DownloadRepositoryImpl
    ): DownloadRepository = impl
}
