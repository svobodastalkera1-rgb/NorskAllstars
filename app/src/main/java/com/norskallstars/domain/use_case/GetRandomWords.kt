package com.norskallstars.domain.use_case

import com.norskallstars.domain.repository.WordRepository
import javax.inject.Inject

class GetRandomWords @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(limit: Int): List<com.norskallstars.data.local.entities.WordEntity> {
        return repository.getRandomWords(limit)
    }
}