package com.currupt.reflame.feature.reading

import com.currupt.reflame.core.model.ContentTitle
import com.currupt.reflame.core.model.Chapter

sealed class ReadingHomeState {
    object Loading : ReadingHomeState()
    data class Success(val titles: List<ContentTitle>) : ReadingHomeState()
    data class Error(val message: String) : ReadingHomeState()
}

sealed class TitleDetailsState {
    object Loading : TitleDetailsState()
    data class Success(val title: ContentTitle, val chapters: List<Chapter>) : TitleDetailsState()
    data class Error(val message: String) : TitleDetailsState()
}
