package com.norskallstars.data.repository

import com.norskallstars.data.local.dao.WordDao
import com.norskallstars.data.local.entities.WordEntity
import com.norskallstars.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val wordDao: WordDao
) : WordRepository {
    override fun getAllWords(): Flow<List<WordEntity>> = wordDao.getAllWords()
    override suspend fun getWordById(wordId: Long): WordEntity? = wordDao.getWordById(wordId)
    override fun getWordsByDifficulty(difficulty: String): Flow<List<WordEntity>> = wordDao.getWordsByDifficulty(difficulty)
    override fun getWordsToLearn(): Flow<List<WordEntity>> = wordDao.getWordsToLearn()
    override suspend fun insertWord(word: WordEntity): Long = wordDao.insertWord(word)
    override suspend fun insertAllWords(words: List<WordEntity>) = wordDao.insertAllWords(words)
    override suspend fun updateWord(word: WordEntity) = wordDao.updateWord(word)
    override suspend fun deleteAllWords() = wordDao.deleteAllWords()
    override fun getLearnedWordsCount(): Flow<Int> = wordDao.getLearnedWordsCount()
    override suspend fun getRandomWords(limit: Int): List<WordEntity> = wordDao.getRandomWords(limit)
}