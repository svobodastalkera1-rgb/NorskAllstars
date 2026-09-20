package com.norskallstars.di

import com.norskallstars.data.repository.SentenceRepositoryImpl
import com.norskallstars.data.repository.UserStatsRepositoryImpl
import com.norskallstars.data.repository.WordRepositoryImpl
import com.norskallstars.domain.repository.SentenceRepository
import com.norskallstars.domain.repository.UserStatsRepository
import com.norskallstars.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWordRepository(impl: WordRepositoryImpl): WordRepository

    @Binds
    @Singleton
    abstract fun bindSentenceRepository(impl: SentenceRepositoryImpl): SentenceRepository

    @Binds
    @Singleton
    abstract fun bindUserStatsRepository(impl: UserStatsRepositoryImpl): UserStatsRepository
}