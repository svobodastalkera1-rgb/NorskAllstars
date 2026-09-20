package com.norskallstars.di

import android.content.Context
import com.norskallstars.domain.use_case.GetLearnedWordsCount
import com.norskallstars.domain.use_case.GetTotalWordsCount
import com.norskallstars.domain.use_case.GetRandomWords
import com.norskallstars.domain.use_case.GetSentencesForWord
import com.norskallstars.domain.use_case.GetUserStats
import com.norskallstars.domain.use_case.InitializeDatabase
import com.norskallstars.domain.use_case.UpdateUserStats
import com.norskallstars.domain.repository.SentenceRepository
import com.norskallstars.domain.repository.UserStatsRepository
import com.norskallstars.domain.repository.WordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetUserStats(repository: UserStatsRepository): GetUserStats {
        return GetUserStats(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserStats(repository: UserStatsRepository): UpdateUserStats {
        return UpdateUserStats(repository)
    }

    @Provides
    @Singleton
    fun provideGetRandomWords(repository: WordRepository): GetRandomWords {
        return GetRandomWords(repository)
    }

    @Provides
    @Singleton
    fun provideGetLearnedWordsCount(repository: WordRepository): GetLearnedWordsCount {
        return GetLearnedWordsCount(repository)
    }

    @Provides
    @Singleton
    fun provideGetTotalWordsCount(repository: WordRepository): GetTotalWordsCount {
        return GetTotalWordsCount(repository)
    }

    @Provides
    @Singleton
    fun provideGetSentencesForWord(repository: SentenceRepository): GetSentencesForWord {
        return GetSentencesForWord(repository)
    }

    @Provides
    @Singleton
    fun provideInitializeDatabase(
        wordRepository: WordRepository,
        sentenceRepository: SentenceRepository,
        @ApplicationContext context: Context
    ): InitializeDatabase {
        return InitializeDatabase(wordRepository, sentenceRepository, context)
    }
}