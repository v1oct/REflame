package com.currupt.reflame.core.availability

/**
 * Service for checking content availability and scheduling.
 */
object AvailabilityService {

    /**
     * Checks if a piece of content is currently available based on its schedule.
     */
    fun isAvailable(startsAt: Long?, expiresAt: Long?, isActive: Boolean): Boolean {
        if (!isActive) return false
        
        val now = System.currentTimeMillis()
        
        val hasStarted = startsAt == null || now >= startsAt
        val hasNotExpired = expiresAt == null || now < expiresAt
        
        return hasStarted && hasNotExpired
    }
}
