package com.norskallstars.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norskallstars.domain.use_case.GetLearnedWordsCount
import com.norskallstars.domain.use_case.GetTotalWordsCount
import com.norskallstars.domain.use_case.GetUserStats
import com.norskallstars.domain.use_case.InitializeDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLearnedWordsCount: GetLearnedWordsCount,
    private val getTotalWordsCount: GetTotalWordsCount,
    private val getUserStats: GetUserStats,
    private val initializeDatabase: InitializeDatabase
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    private var collectionJob: Job? = null

    init {
        loadProgressData()
    }

    fun resetLoadingState() {
        _state.value = _state.value.copy(isLoading = true)
        loadProgressData()
    }

    private fun loadProgressData() {
        collectionJob?.cancel()
        collectionJob = viewModelScope.launch {
            initializeDatabase()
            
            launch {
                getLearnedWordsCount().collect { count ->
                    _state.value = _state.value.copy(
                        learnedWordsCount = count,
                        isLoading = false
                    )
                }
            }

            launch {
                getTotalWordsCount().collect { count ->
                    _state.value = _state.value.copy(
                        totalWordsCount = count
                    )
                }
            }

            launch {
                getUserStats().collect { stats ->
                    val totalMinutes = stats.totalLearningTimeMillis / 1000 / 60
                    val hours = totalMinutes / 60
                    val minutes = totalMinutes % 60
                    val formattedTime = String.format(Locale.US, "%02d:%02d", hours, minutes)

                    _state.value = _state.value.copy(
                        dailyStreak = stats.dailyStreak,
                        learningTime = formattedTime
                    )
                }
            }
        }
    }
}
