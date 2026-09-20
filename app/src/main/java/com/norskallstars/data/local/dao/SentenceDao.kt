package com.norskallstars.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.norskallstars.data.local.entities.SentenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SentenceDao {
    @Query("SELECT * FROM sentences WHERE word_id = :wordId")
    fun getSentencesForWord(wordId: Long): Flow<List<SentenceEntity>>

    @Insert
    suspend fun insertSentence(sentence: SentenceEntity): Long

    @Insert
    suspend fun insertAllSentences(sentences: List<SentenceEntity>)

    @Query("DELETE FROM sentences")
    suspend fun deleteAllSentences()
}