package com.norskallstars.domain.repository

import com.norskallstars.data.local.entities.WordEntity
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun getAllWords(): Flow<List<WordEntity>>
    suspend fun getWordById(wordId: Long): WordEntity?
    fun getWordsByDifficulty(difficulty: String): Flow<List<WordEntity>>
    fun getWordsToLearn(): Flow<List<WordEntity>>
    suspend fun insertWord(word: WordEntity): Long
    suspend fun insertAllWords(words: List<WordEntity>)
    suspend fun updateWord(word: WordEntity)
    suspend fun deleteAllWords()
    fun getLearnedWordsCount(): Flow<Int>
    suspend fun getRandomWords(limit: Int): List<WordEntity>
}