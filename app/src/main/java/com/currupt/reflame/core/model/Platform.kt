package com.currupt.reflame.core.model

/**
 * Universal platform announcement.
 */
data class Announcement(
    val id: String,
    val title: String,
    val message: String,
    val type: AnnouncementType = AnnouncementType.NEWS,
    val priority: Int = 0,
    val imageUrl: String? = null,
    val actionUrl: String? = null,
    val startsAt: Long,
    val expiresAt: Long? = null,
    val isActive: Boolean = true
)

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
data class ContentSection(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val type: SectionType = SectionType.RAIL,
    val vertical: Vertical? = null, // Null means platform-wide
    val filterQuery: String = "", // e.g. "isHot=true"
    val ordering: String = "trending",
    val limit: Int = 10,
    val startsAt: Long? = null,
    val expiresAt: Long? = null,
    val isActive: Boolean = true
)

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
data class Notification(
    val id: String,
    val userId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val referenceId: String? = null, // e.g. contentId or transactionId
    val isRead: Boolean = false,
    val createdAt: Long
)

enum class NotificationType {
    CONTENT_RELEASE,
    REWARD_ACQUIRED,
    ECONOMY_ADJUSTMENT,
    SYSTEM_ANNOUNCEMENT,
    SOCIAL_INTERACTION
}
