package com.norskallstars.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "daily_streak") val dailyStreak: Int = 0,
    @ColumnInfo(name = "last_study_timestamp") val lastStudyTimestamp: Long = 0,
    @ColumnInfo(name = "total_learning_time_millis") val totalLearningTimeMillis: Long = 0
)
