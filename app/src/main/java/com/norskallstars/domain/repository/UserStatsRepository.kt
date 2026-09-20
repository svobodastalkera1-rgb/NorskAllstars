package com.norskallstars.domain.repository

import com.norskallstars.data.local.entities.UserStatsEntity
import kotlinx.coroutines.flow.Flow

interface UserStatsRepository {
    fun getUserStats(): Flow<UserStatsEntity?>
    suspend fun insertOrUpdate(stats: UserStatsEntity)
}
