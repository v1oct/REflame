package com.currupt.reflame.feature.reading

import com.currupt.reflame.core.model.ContentTitle
import com.currupt.reflame.core.model.Chapter
import com.currupt.reflame.core.model.ContentSection

sealed class ReadingHomeState {
    object Loading : ReadingHomeState()
    data class Success(val sections: List<ReadingHomeSection>) : ReadingHomeState()
    data class Error(val message: String) : ReadingHomeState()
}

data class ReadingHomeSection(
    val section: ContentSection,
    val titles: List<ContentTitle>
)

sealed class TitleDetailsState {
    object Loading : TitleDetailsState()
    data class Success(val title: ContentTitle, val chapters: List<Chapter>) : TitleDetailsState()
    data class Error(val message: String) : TitleDetailsState()
}
