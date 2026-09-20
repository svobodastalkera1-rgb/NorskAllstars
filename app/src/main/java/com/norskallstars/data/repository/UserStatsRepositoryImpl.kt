package com.norskallstars.data.repository

import com.norskallstars.data.local.dao.UserStatsDao
import com.norskallstars.data.local.entities.UserStatsEntity
import com.norskallstars.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserStatsRepositoryImpl @Inject constructor(
    private val userStatsDao: UserStatsDao
) : UserStatsRepository {
    override fun getUserStats(): Flow<UserStatsEntity?> = userStatsDao.getUserStats()
    override suspend fun insertOrUpdate(stats: UserStatsEntity) = userStatsDao.insertOrUpdate(stats)
}
