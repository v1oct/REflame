package com.currupt.reflame.feature.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currupt.reflame.core.content.ContentRepository
import com.currupt.reflame.core.content.SupabaseContentRepository
import com.currupt.reflame.core.content.SectionRepository
import com.currupt.reflame.core.content.SupabaseSectionRepository
import com.currupt.reflame.core.collections.CollectionRepository
import com.currupt.reflame.core.collections.SupabaseCollectionRepository
import com.currupt.reflame.core.model.Vertical
import com.currupt.reflame.core.model.SectionSourceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReadingHomeViewModel(
    private val contentRepository: ContentRepository = SupabaseContentRepository(),
    private val sectionRepository: SectionRepository = SupabaseSectionRepository(),
    private val collectionRepository: CollectionRepository = SupabaseCollectionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReadingHomeState>(ReadingHomeState.Loading)
    val uiState: StateFlow<ReadingHomeState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = ReadingHomeState.Loading
            try {
                // 1. Fetch Sections
                val sections = sectionRepository.getSections(Vertical.READING)
                
                // 2. Fetch Titles for each section based on its source type
                val homeSections = sections.map { section ->
                    val titles = when (section.sourceType) {
                        SectionSourceType.COLLECTION -> {
                            section.collectionId?.let { collectionRepository.getCollectionTitles(it) } ?: emptyList()
                        }
                        else -> {
                            contentRepository.getTitlesForSection(section)
                        }
                    }
                    ReadingHomeSection(section, titles)
                }
                
                _uiState.value = ReadingHomeState.Success(homeSections)
            } catch (e: Exception) {
                _uiState.value = ReadingHomeState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
