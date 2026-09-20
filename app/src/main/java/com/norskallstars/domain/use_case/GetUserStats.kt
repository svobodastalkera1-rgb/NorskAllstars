package com.norskallstars.domain.use_case

import com.norskallstars.data.local.entities.UserStatsEntity
import com.norskallstars.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserStats @Inject constructor(
    private val repository: UserStatsRepository
) {
    operator fun invoke(): Flow<UserStatsEntity> {
        return repository.getUserStats().map { it ?: UserStatsEntity(id = 0) }
    }
}
