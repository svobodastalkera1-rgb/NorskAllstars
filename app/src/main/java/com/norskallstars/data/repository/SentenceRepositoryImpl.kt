package com.norskallstars.data.repository

import com.norskallstars.data.local.dao.SentenceDao
import com.norskallstars.data.local.entities.SentenceEntity
import com.norskallstars.domain.repository.SentenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SentenceRepositoryImpl @Inject constructor(
    private val sentenceDao: SentenceDao
) : SentenceRepository {
    override fun getSentencesForWord(wordId: Long): Flow<List<SentenceEntity>> =
        sentenceDao.getSentencesForWord(wordId)

    override suspend fun insertSentence(sentence: SentenceEntity): Long =
        sentenceDao.insertSentence(sentence)

    override suspend fun insertAllSentences(sentences: List<SentenceEntity>) =
        sentenceDao.insertAllSentences(sentences)

    override suspend fun deleteAllSentences() =
        sentenceDao.deleteAllSentences()
}