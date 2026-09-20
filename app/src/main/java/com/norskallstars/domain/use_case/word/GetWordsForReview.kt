package com.norskallstars.domain.use_case.word

import com.norskallstars.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class GetWordsForReview(
    private val repository: WordRepository
) {
    operator fun invoke(currentTime: Long): Flow<List<com.norskallstars.data.local.entities.WordEntity>> {
        return repository.getWordsForReview(currentTime)
    }
}