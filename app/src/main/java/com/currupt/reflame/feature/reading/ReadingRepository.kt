package com.currupt.reflame.feature.reading

import com.currupt.reflame.core.content.ContentRepository
import com.currupt.reflame.core.model.*

/**
 * Vertical-specific repository for Reading functionality.
 */
class ReadingRepository(
    private val contentRepository: ContentRepository
) {
    suspend fun getReadingTitles(): List<ContentTitle> {
        return contentRepository.getTitles(Vertical.READING)
    }

    suspend fun getTitleDetails(id: String): ContentTitle? {
        return contentRepository.getTitleDetails(id)
    }

    suspend fun getChapters(titleId: String): List<Chapter> {
        return contentRepository.getChapters(titleId)
    }
}
