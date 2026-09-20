package com.norskallstars.ui.main

data class MainScreenState(
    val learnedWordsCount: Int = 0,
    val totalWordsCount: Int = 0,
    val dailyStreak: Int = 0,
    val learningTime: String = "00:00",
    val a1Progress: Float = 0f,
    val a2Progress: Float = 0f,
    val b1Progress: Float = 0f,
    val b2Progress: Float = 0f,
    val isLoading: Boolean = true
)