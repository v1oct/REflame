package com.currupt.reflame.core.model

/**
 * User wallet for platform economy.
 */
data class Wallet(
    val userId: String,
    val balance: Int = 0
)

/**
 * Auditable transaction for any RΞflame Coin movement.
 */
data class CoinTransaction(
    val id: String,
    val userId: String,
    val type: TransactionType,
    val amount: Int,
    val source: String, // PURCHASE, REWARD_SYSTEM, etc.
    val referenceId: String? = null, // OrderId, RedemptionId, ChapterId
    val createdAt: Long,
    val metadata: Map<String, String> = emptyMap()
)

enum class TransactionType {
    PURCHASE,
    REWARD,
    REDEMPTION,
    SPEND,
    REFUND,
    ADMIN_ADJUSTMENT
}
