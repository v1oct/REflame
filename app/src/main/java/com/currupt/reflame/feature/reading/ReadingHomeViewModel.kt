package com.currupt.reflame.feature.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currupt.reflame.core.content.SupabaseContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReadingHomeViewModel(
    private val repository: ReadingRepository = ReadingRepository(SupabaseContentRepository())
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReadingHomeState>(ReadingHomeState.Loading)
    val uiState: StateFlow<ReadingHomeState> = _uiState.asStateFlow()

    init {
        loadTitles()
    }

    fun loadTitles() {
        viewModelScope.launch {
            _uiState.value = ReadingHomeState.Loading
            try {
                val titles = repository.getReadingTitles()
                _uiState.value = ReadingHomeState.Success(titles)
            } catch (e: Exception) {
                _uiState.value = ReadingHomeState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
