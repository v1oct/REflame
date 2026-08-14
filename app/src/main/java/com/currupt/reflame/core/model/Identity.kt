package com.currupt.reflame.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Universal User account entity.
 * Authentication credentials should be managed by the identity provider (e.g. Firebase Auth).
 */
@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
    val role: UserRole = UserRole.USER,
    val status: UserStatus = UserStatus.ACTIVE
)

/**
 * Public-facing profile data.
 */
@Serializable
data class Profile(
    @SerialName("user_id") val userId: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("avatar_url") val avatarUrl: String = "",
    @SerialName("banner_url") val bannerUrl: String = "",
    val bio: String = "",
    @SerialName("active_titles") val activeTitles: List<String> = emptyList(),
    val badges: List<String> = emptyList(),
    val stats: ProfileStats = ProfileStats(),
    val customization: Map<String, String> = emptyMap()
)

@Serializable
data class ProfileStats(
    @SerialName("titles_read") val titlesRead: Int = 0,
    @SerialName("chapters_read") val chaptersRead: Int = 0,
    val level: Int = 1,
    val xp: Long = 0
)

enum class UserRole {
    USER,
    MODERATOR,
    EDITOR,
    ADMIN,
    OWNER
}

enum class UserStatus {
    ACTIVE,
    SUSPENDED,
    BANNED,
    DELETED
}
