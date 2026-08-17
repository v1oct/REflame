package com.currupt.reflame.feature.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currupt.reflame.core.content.SupabaseContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicHomeViewModel(
    private val repository: MusicRepository = MusicRepository(SupabaseContentRepository())
) : ViewModel() {

    private val _uiState = MutableStateFlow<MusicHomeState>(MusicHomeState.Loading)
    val uiState: StateFlow<MusicHomeState> = _uiState.asStateFlow()

    init {
        loadMusic()
    }

    fun loadMusic() {
        viewModelScope.launch {
            _uiState.value = MusicHomeState.Loading
            try {
                val titles = repository.getMusicTitles()
                _uiState.value = MusicHomeState.Success(titles)
            } catch (e: Exception) {
                _uiState.value = MusicHomeState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
