package com.norskallstars.domain.use_case.word

import com.norskallstars.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class GetLearnedWordsCount(
    private val repository: WordRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getLearnedWordsCount()
    }
}