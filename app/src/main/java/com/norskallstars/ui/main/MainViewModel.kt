package com.norskallstars.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norskallstars.domain.use_case.GetLearnedWordsCount
import com.norskallstars.domain.use_case.InitializeDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLearnedWordsCount: GetLearnedWordsCount,
    private val initializeDatabase: InitializeDatabase
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    init {
        loadProgressData()
    }

    fun resetLoadingState() {
        // Сбрасываем состояние загрузки при возврате на экран
        _state.value = _state.value.copy(isLoading = true)
        loadProgressData()
    }

    private fun loadProgressData() {
        viewModelScope.launch {
            initializeDatabase()
            delay(1000)

            getLearnedWordsCount().collect { count ->
                _state.value = _state.value.copy(
                    learnedWordsCount = count,
                    isLoading = false
                )
            }
        }
    }
}