package com.norskallstars.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.norskallstars.data.local.entities.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE id = :wordId")
    suspend fun getWordById(wordId: Long): WordEntity?

    @Query("SELECT * FROM words WHERE difficulty = :difficulty")
    fun getWordsByDifficulty(difficulty: String): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE is_learned = 0")
    fun getWordsToLearn(): Flow<List<WordEntity>>

    @Insert
    suspend fun insertWord(word: WordEntity): Long

    @Insert
    suspend fun insertAllWords(words: List<WordEntity>)

    @Update
    suspend fun updateWord(word: WordEntity)

    @Query("DELETE FROM words")
    suspend fun deleteAllWords()

    // Исправленный метод без конфликта
    @Query("SELECT COUNT(*) FROM words WHERE is_learned = 1")
    fun getLearnedWordsCount(): Flow<Int>

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomWords(limit: Int): List<WordEntity>

    @Query("SELECT * FROM words WHERE next_review_date <= :currentTime")
    fun getWordsForReview(currentTime: Long): Flow<List<WordEntity>>
}