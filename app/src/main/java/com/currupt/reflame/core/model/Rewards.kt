package com.currupt.reflame.core.model

/**
 * Platform redemption code.
 */
data class RedemptionCode(
    val id: String,
    val code: String,
    val type: RewardType,
    val rewardValue: String, // Value or JSON metadata
    val maxUses: Int = 1,
    val currentUses: Int = 0,
    val startsAt: Long,
    val expiresAt: Long? = null,
    val isActive: Boolean = true,
    val requirements: Map<String, Any> = emptyMap()
)

data class CodeRedemption(
    val id: String,
    val codeId: String,
    val userId: String,
    val redeemedAt: Long,
    val grantedReward: String
)

enum class RewardType {
    COINS,
    BADGE,
    TITLE_ACCESS,
    PREMIUM_STATUS,
    EVENT_ITEM
}

/**
 * Universal platform badge.
 */
data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String,
    val rarity: BadgeRarity = BadgeRarity.COMMON,
    val price: Int = 0, // In coins, if purchasable
    val isActive: Boolean = true,
    val startsAt: Long? = null,
    val expiresAt: Long? = null,
    val benefits: Map<String, Any> = emptyMap()
)

data class UserBadge(
    val id: String,
    val userId: String,
    val badgeId: String,
    val acquiredAt: Long,
    val expiresAt: Long? = null,
    val source: String // REDEMPTION, PURCHASE, ADMIN, EVENT
)

enum class BadgeRarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    MYTHIC,
    EXCLUSIVE
}
