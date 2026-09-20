package com.norskallstars.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.norskallstars.data.local.dao.AchievementDao
import com.norskallstars.data.local.dao.SentenceDao
import com.norskallstars.data.local.dao.WordDao
import com.norskallstars.data.local.entities.AchievementEntity
import com.norskallstars.data.local.entities.SentenceEntity
import com.norskallstars.data.local.entities.WordEntity

@Database(
    entities = [WordEntity::class, SentenceEntity::class, AchievementEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NorskDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun sentenceDao(): SentenceDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var Instance: NorskDatabase? = null

        fun getDatabase(context: Context): NorskDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    NorskDatabase::class.java,
                    "norsk_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}