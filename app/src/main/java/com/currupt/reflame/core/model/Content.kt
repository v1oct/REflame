package com.currupt.reflame.core.model

import androidx.compose.ui.graphics.Color

/**
 * Universal content verticals supported by RΞflame.
 */
enum class Vertical {
    READING,
    ANIME,
    MOVIES
}

/**
 * High-level content types within verticals.
 */
enum class ContentType {
    MANHWA,
    MANGA,
    MANHUA,
    WEBCOMIC,
    COMIC,
    SERIES,
    MOVIE,
    OVA
}

/**
 * Universal abstraction for any content title in the RΞflame ecosystem.
 */
data class ContentTitle(
    val id: String,
    val title: String,
    val description: String,
    val coverUrl: String = "",
    val backdropUrl: String = "",
    val vertical: Vertical,
    val type: ContentType,
    val genres: List<String>,
    val creators: List<Creator> = emptyList(),
    val status: ContentStatus = ContentStatus.ONGOING,
    val releaseInfo: String = "",
    val rating: String = "",
    val artworkColor: Color = Color.DarkGray,
    val metadata: Map<String, Any> = emptyMap()
)

data class Creator(
    val name: String,
    val role: String // Author, Artist, Director, etc.
)

enum class ContentStatus {
    ONGOING,
    COMPLETED,
    HIATUS,
    CANCELLED,
    UPCOMING
}

/**
 * Base class for vertical-specific content units (Chapters, Episodes, etc.).
 */
interface ContentUnit {
    val id: String
    val titleId: String
    val number: Double
    val title: String
    val releaseDate: String
    val accessState: AccessState
}

enum class AccessState {
    AVAILABLE,
    NEW,
    EARLY_ACCESS,
    LOCKED
}
