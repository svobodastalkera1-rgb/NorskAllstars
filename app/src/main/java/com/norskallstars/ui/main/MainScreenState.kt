package com.norskallstars.ui.main

data class MainScreenState(
    val learnedWordsCount: Int = 0,
    val totalWordsCount: Int = 1000,
    val dailyStreak: Int = 5,
    val learningTime: String = "12:45",
    val a1Progress: Float = 0.25f,
    val a2Progress: Float = 0.1f,
    val b1Progress: Float = 0.05f,
    val b2Progress: Float = 0.02f,
    val isLoading: Boolean = true
)