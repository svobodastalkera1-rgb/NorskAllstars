package com.norskallstars.ui.learning

import com.norskallstars.data.local.entities.WordEntity

sealed class LearningScreenState {
    object Loading : LearningScreenState()
    data class Question(
        val word: WordEntity,
        val options: List<String>,
        val correctAnswer: String,
        val sentence: String? = null
    ) : LearningScreenState()
    data class Result(
        val isCorrect: Boolean,
        val selectedAnswer: String,
        val word: WordEntity,
        val options: List<String>,
        val correctAnswer: String,
        val sentence: String?
    ) : LearningScreenState()
    data class Error(val message: String) : LearningScreenState()
}