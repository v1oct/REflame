package com.currupt.reflame.feature.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currupt.reflame.core.content.SupabaseContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class TitleDetailsViewModel(
    private val titleId: String,
    private val repository: ReadingRepository = ReadingRepository(SupabaseContentRepository())
) : ViewModel() {

    companion object {
        fun provideFactory(titleId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TitleDetailsViewModel(titleId)
            }
        }
    }

    private val _uiState = MutableStateFlow<TitleDetailsState>(TitleDetailsState.Loading)
    val uiState: StateFlow<TitleDetailsState> = _uiState.asStateFlow()

    init {
        loadDetails()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _uiState.value = TitleDetailsState.Loading
            try {
                val title = repository.getTitleDetails(titleId)
                if (title != null) {
                    val chapters = repository.getChapters(titleId)
                    _uiState.value = TitleDetailsState.Success(title, chapters)
                } else {
                    _uiState.value = TitleDetailsState.Error("Title not found")
                }
            } catch (e: Exception) {
                _uiState.value = TitleDetailsState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
