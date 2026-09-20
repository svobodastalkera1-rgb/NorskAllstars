package com.norskallstars.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "norwegian") val norwegian: String,
    @ColumnInfo(name = "russian") val russian: String,
    @ColumnInfo(name = "part_of_speech") val partOfSpeech: String,
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "is_learned") val isLearned: Boolean = false,
    @ColumnInfo(name = "correct_answers") val correctAnswers: Int = 0,
    @ColumnInfo(name = "wrong_answers") val wrongAnswers: Int = 0,
    @ColumnInfo(name = "next_review_date") val nextReviewDate: Long = System.currentTimeMillis()
)