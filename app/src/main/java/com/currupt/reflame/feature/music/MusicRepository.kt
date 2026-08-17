package com.currupt.reflame.feature.music

import com.currupt.reflame.core.content.ContentRepository
import com.currupt.reflame.core.model.*

/**
 * Vertical-specific repository for Music functionality.
 */
class MusicRepository(
    private val contentRepository: ContentRepository
) {
    suspend fun getMusicTitles(): List<ContentTitle> {
        return contentRepository.getTitles(Vertical.MUSIC)
    }
}
