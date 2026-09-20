package com.norskallstars.domain.use_case

import com.norskallstars.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLearnedWordsCount @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getLearnedWordsCount()
    }
}