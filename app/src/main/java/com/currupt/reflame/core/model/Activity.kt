package com.currupt.reflame.core.model

/**
 * Entry in the user's universal library.
 */
data class LibraryEntry(
    val userId: String,
    val contentId: String,
    val vertical: Vertical,
    val addedAt: Long,
    val isFavorite: Boolean = false,
    val collectionName: String? = null
)

/**
 * Granular progress for a specific content unit (Chapter/Episode).
 */
data class ContentProgress(
    val userId: String,
    val contentId: String,
    val unitId: String, // ChapterId, EpisodeId, etc.
    val percentage: Int, // 0-100
    val lastPosition: String = "", // Page number, timestamp, etc.
    val updatedAt: Long,
    val isCompleted: Boolean = false
)

/**
 * Universal viewing/reading history.
 */
data class ViewingHistory(
    val userId: String,
    val contentId: String,
    val unitId: String,
    val accessedAt: Long,
    val progressSnapshot: Int
)
