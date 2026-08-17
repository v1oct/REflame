package com.currupt.reflame.feature.music

import com.currupt.reflame.core.model.ContentTitle

sealed class MusicHomeState {
    object Loading : MusicHomeState()
    data class Success(val titles: List<ContentTitle>) : MusicHomeState()
    data class Error(val message: String) : MusicHomeState()
}
