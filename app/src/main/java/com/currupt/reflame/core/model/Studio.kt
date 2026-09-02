package com.currupt.reflame.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProjectType {
    GAME,
    APP,
    EXPERIMENT
}

@Serializable
enum class ProjectStatus {
    CONCEPT,
    IN_DEVELOPMENT,
    BETA,
    RELEASED,
    ARCHIVED
}

@Serializable
data class Project(
    val id: String,
    val title: String,
    val slug: String,
    val description: String,
    val type: ProjectType,
    val status: ProjectStatus,
    @SerialName("cover_url") val coverUrl: String = "",
    @SerialName("hero_url") val heroUrl: String = "",
    @SerialName("hero_video_url") val heroVideoUrl: String? = null,
    val progress: Int = 0,
    @SerialName("is_featured") val isFeatured: Boolean = false,
    @SerialName("release_info") val releaseInfo: String = "",
    @SerialName("external_links") val externalLinks: Map<String, String> = emptyMap(),
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class Release(
    val id: String,
    @SerialName("project_id") val projectId: String,
    val version: String,
    val title: String,
    val description: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("is_published") val isPublished: Boolean = false
)

@Serializable
data class DevelopmentLog(
    val id: String,
    @SerialName("project_id") val projectId: String,
    val title: String,
    val content: String,
    val timestamp: String,
    @SerialName("is_published") val isPublished: Boolean = false
)

@Serializable
enum class SectionType {
    FEATURED,
    RECENT_RELEASES,
    GAMES,
    APPS,
    IN_DEVELOPMENT,
    EXPERIMENTS,
    CUSTOM
}

@Serializable
data class StudioSection(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val type: SectionType,
    val priority: Int = 0,
    @SerialName("is_visible") val isVisible: Boolean = true,
    @SerialName("availability_starts_at") val startsAt: Long? = null,
    @SerialName("availability_expires_at") val expiresAt: Long? = null,
    @SerialName("query_config") val queryConfig: Map<String, String> = emptyMap()
)

@Serializable
data class Announcement(
    val id: String,
    val title: String,
    val description: String,
    val type: String, // NEWS, UPDATE, EVENT
    @SerialName("artwork_url") val artworkUrl: String? = null,
    @SerialName("is_published") val isPublished: Boolean = false,
    @SerialName("published_at") val publishedAt: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class Notification(
    val id: String,
    @SerialName("user_id") val userId: String,
    val title: String,
    val message: String,
    val type: String,
    @SerialName("is_read") val isRead: Boolean = false,
    @SerialName("created_at") val createdAt: Long
)
