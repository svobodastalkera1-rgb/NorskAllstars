package com.norskallstars.domain.repository

import com.norskallstars.data.local.entities.AchievementEntity
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    suspend fun insertAchievement(achievement: AchievementEntity)
    suspend fun insertAllAchievements(achievements: List<AchievementEntity>)
    suspend fun updateAchievement(achievement: AchievementEntity)
    fun getAllAchievements(): Flow<List<AchievementEntity>>
    fun getAchievedAchievements(): Flow<List<AchievementEntity>>
    suspend fun getAchievementById(id: Long): AchievementEntity?
}