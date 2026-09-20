package com.norskallstars.data.repository

import com.norskallstars.data.local.NorskDatabase
import com.norskallstars.data.local.entities.AchievementEntity
import com.norskallstars.domain.repository.AchievementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AchievementRepositoryImpl @Inject constructor(
    private val database: NorskDatabase
) : AchievementRepository {
    private val achievementDao = database.achievementDao()

    override suspend fun insertAchievement(achievement: AchievementEntity) {
        achievementDao.insertAchievement(achievement)
    }

    override suspend fun insertAllAchievements(achievements: List<AchievementEntity>) {
        achievementDao.insertAllAchievements(achievements)
    }

    override suspend fun updateAchievement(achievement: AchievementEntity) {
        achievementDao.updateAchievement(achievement)
    }

    override fun getAllAchievements(): Flow<List<AchievementEntity>> {
        return achievementDao.getAllAchievements()
    }

    override fun getAchievedAchievements(): Flow<List<AchievementEntity>> {
        return achievementDao.getAchievedAchievements()
    }

    override suspend fun getAchievementById(id: Long): AchievementEntity? {
        return achievementDao.getAchievementById(id)
    }
}