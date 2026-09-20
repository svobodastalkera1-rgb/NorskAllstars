package com.norskallstars.domain.use_case

import com.norskallstars.domain.repository.SentenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSentencesForWord @Inject constructor(
    private val repository: SentenceRepository
) {
    operator fun invoke(wordId: Long): Flow<List<com.norskallstars.data.local.entities.SentenceEntity>> {
        return repository.getSentencesForWord(wordId)
    }
}