package com.norskallstars.domain.use_case

import com.norskallstars.data.local.entities.UserStatsEntity
import com.norskallstars.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

class UpdateUserStats @Inject constructor(
    private val repository: UserStatsRepository
) {
    suspend operator fun invoke(durationMillis: Long) {
        val currentStats = repository.getUserStats().first() ?: UserStatsEntity(id = 0)
        
        val currentTimestamp = System.currentTimeMillis()
        
        val newStreak = if (currentStats.lastStudyTimestamp == 0L) {
            1
        } else {
            val calToday = Calendar.getInstance().apply { timeInMillis = currentTimestamp }
            val calLast = Calendar.getInstance().apply { timeInMillis = currentStats.lastStudyTimestamp }
            
            val isSameDay = calToday.get(Calendar.YEAR) == calLast.get(Calendar.YEAR) &&
                    calToday.get(Calendar.DAY_OF_YEAR) == calLast.get(Calendar.DAY_OF_YEAR)
            
            if (isSameDay) {
                currentStats.dailyStreak
            } else {
                // Check if last study was yesterday
                val calYesterday = Calendar.getInstance().apply {
                    timeInMillis = currentTimestamp
                    add(Calendar.DAY_OF_YEAR, -1)
                }
                val isYesterday = calYesterday.get(Calendar.YEAR) == calLast.get(Calendar.YEAR) &&
                        calYesterday.get(Calendar.DAY_OF_YEAR) == calLast.get(Calendar.DAY_OF_YEAR)
                
                if (isYesterday) {
                    currentStats.dailyStreak + 1
                } else {
                    1
                }
            }
        }

        val updatedStats = currentStats.copy(
            dailyStreak = newStreak,
            lastStudyTimestamp = currentTimestamp,
            totalLearningTimeMillis = currentStats.totalLearningTimeMillis + durationMillis
        )

        repository.insertOrUpdate(updatedStats)
    }
}
