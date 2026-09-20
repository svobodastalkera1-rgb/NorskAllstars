package com.norskallstars.di

import android.content.Context
import androidx.room.Room
import com.norskallstars.data.local.NorskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNorskDatabase(@ApplicationContext context: Context): NorskDatabase {
        return Room.databaseBuilder(
            context,
            NorskDatabase::class.java,
            "norsk_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWordDao(database: NorskDatabase) = database.wordDao()

    @Provides
    @Singleton
    fun provideSentenceDao(database: NorskDatabase) = database.sentenceDao()

    @Provides
    @Singleton
    fun provideAchievementDao(database: NorskDatabase) = database.achievementDao()

    @Provides
    @Singleton
    fun provideUserStatsDao(database: NorskDatabase) = database.userStatsDao()
}