package com.example.oriencoop_score.api

import android.content.Context
import com.example.oriencoop_score.HandleNotifications
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationHelper(@ApplicationContext context: Context): HandleNotifications {
        return HandleNotifications(context)
    }
}