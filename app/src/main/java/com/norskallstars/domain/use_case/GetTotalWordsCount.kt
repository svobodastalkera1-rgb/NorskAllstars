package com.norskallstars.domain.use_case

import com.norskallstars.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTotalWordsCount @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getAllWords().map { it.size }
    }
}
