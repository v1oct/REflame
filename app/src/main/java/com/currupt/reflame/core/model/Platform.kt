package com.currupt.reflame.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Universal platform announcement.
 */
@Serializable
data class Announcement(
    val id: String,
    val title: String,
    val message: String,
    val type: AnnouncementType = AnnouncementType.NEWS,
    val priority: Int = 0,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("action_url") val actionUrl: String? = null,
    @SerialName("starts_at") val startsAt: Long,
    @SerialName("expires_at") val expiresAt: Long? = null,
    @SerialName("is_active") val isActive: Boolean = true
)

@Serializable
enum class AnnouncementType {
    NEWS,
    UPDATE,
    EVENT,
    MAINTENANCE,
    OFFER
}

/**
 * Editorial section for homepages (universal or vertical-specific).
 */
@Serializable
data class ContentSection(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val type: SectionType = SectionType.RAIL,
    val vertical: Vertical? = null,
    @SerialName("source_type") val sourceType: SectionSourceType = SectionSourceType.LATEST_RELEASES,
    @SerialName("collection_id") val collectionId: String? = null,
    @SerialName("filter_query") val filterQuery: String = "",
    val ordering: String = "trending",
    val limit: Int = 10,
    @SerialName("starts_at") val startsAt: Long? = null,
    @SerialName("expires_at") val expiresAt: Long? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    val priority: Int = 0
)

@Serializable
enum class SectionSourceType {
    LATEST_RELEASES,
    TRENDING,
    HOT,
    NEW,
    COLLECTION,
    PERSONALIZED
}

@Serializable
enum class SectionType {
    HERO,
    RAIL,
    GRID,
    LIST,
    BANNER
}

/**
 * Universal user notification.
 */
@Serializable
data class Notification(
    val id: String,
    @SerialName("user_id") val userId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    @SerialName("reference_id") val referenceId: String? = null,
    @SerialName("is_read") val isRead: Boolean = false,
    @SerialName("created_at") val createdAt: Long
)

@Serializable
enum class NotificationType {
    CONTENT_RELEASE,
    REWARD_ACQUIRED,
    ECONOMY_ADJUSTMENT,
    SYSTEM_ANNOUNCEMENT,
    SOCIAL_INTERACTION
}
