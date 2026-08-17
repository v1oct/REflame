package com.currupt.reflame.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Universal content verticals supported by RΞflame.
 */
@Serializable
enum class Vertical {
    READING,
    MUSIC
}

/**
 * High-level content types within verticals.
 */
@Serializable
enum class ContentType {
    MANHWA,
    MANGA,
    MANHUA,
    WEBCOMIC,
    COMIC,
    TRACK,
    ALBUM,
    ARTIST
}

/**
 * Universal abstraction for any content title in the RΞflame ecosystem.
 */
@Serializable
data class ContentTitle(
    val id: String,
    val title: String,
    val description: String,
    @SerialName("cover_url") val coverUrl: String = "",
    @SerialName("backdrop_url") val backdropUrl: String = "",
    val vertical: Vertical,
    @SerialName("content_type") val type: ContentType,
    val genres: List<String> = emptyList(),
    val status: ContentStatus = ContentStatus.ONGOING,
    @SerialName("is_hot") val isHot: Boolean = false,
    @SerialName("is_new") val isNew: Boolean = false,
    @SerialName("is_trending") val isTrending: Boolean = false,
    @SerialName("is_early_access") val isEarlyAccess: Boolean = false,
    @SerialName("artwork_color") val artworkColorHex: String = "#2C3E50",
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
enum class ContentStatus {
    ONGOING,
    COMPLETED,
    HIATUS,
    CANCELLED,
    UPCOMING
}

/**
 * Universal content collection.
 */
@Serializable
data class ContentCollection(
    val id: String,
    val title: String,
    val description: String = "",
    @SerialName("artwork_url") val artworkUrl: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("created_at") val createdAt: String? = null
)

/**
 * Link between a collection and a title.
 */
@Serializable
data class CollectionItem(
    @SerialName("collection_id") val collectionId: String,
    @SerialName("content_id") val contentId: String,
    val priority: Int = 0
)

/**
 * Content unit for a title (e.g. Chapter for Reading, Episode for Anime).
 */
@Serializable
data class Chapter(
    val id: String,
    @SerialName("content_id") val contentId: String,
    val number: Double,
    val title: String? = null,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("access_state") val accessState: String = "AVAILABLE",
    @SerialName("coin_price") val coinPrice: Int = 0
)
