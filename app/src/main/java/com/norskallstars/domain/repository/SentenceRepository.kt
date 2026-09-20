package com.norskallstars.domain.repository

import com.norskallstars.data.local.entities.SentenceEntity
import kotlinx.coroutines.flow.Flow

interface SentenceRepository {
    fun getSentencesForWord(wordId: Long): Flow<List<SentenceEntity>>
    suspend fun insertSentence(sentence: SentenceEntity): Long
    suspend fun insertAllSentences(sentences: List<SentenceEntity>)
    suspend fun deleteAllSentences()
}