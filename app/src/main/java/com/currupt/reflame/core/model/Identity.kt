package com.currupt.reflame.core.model

/**
 * Universal User account entity.
 * Authentication credentials should be managed by the identity provider (e.g. Firebase Auth).
 */
data class User(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: Long,
    val updatedAt: Long,
    val role: UserRole = UserRole.USER,
    val status: UserStatus = UserStatus.ACTIVE
)

/**
 * Public-facing profile data.
 */
data class Profile(
    val userId: String,
    val displayName: String,
    val avatarUrl: String = "",
    val bannerUrl: String = "",
    val bio: String = "",
    val activeTitles: List<String> = emptyList(), // Selected titles/tags to display
    val badges: List<String> = emptyList(),
    val stats: ProfileStats = ProfileStats(),
    val customization: Map<String, String> = emptyMap()
)

data class ProfileStats(
    val titlesRead: Int = 0,
    val chaptersRead: Int = 0,
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
