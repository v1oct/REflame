package com.currupt.reflame.core.auth

import com.currupt.reflame.core.Supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for checking and managing CURRUPT. Studio admin permissions.
 */
class AdminRepository {

    private val auth = Supabase.client.auth
    private val postgrest = Supabase.client.postgrest

    /**
     * Checks if the current authenticated user is an authorized admin.
     * This is server-authoritative by checking the 'profiles' or 'admins' table.
     */
    suspend fun isCurrentUserManager(): Boolean = withContext(Dispatchers.IO) {
        val currentUser = auth.currentUserOrNull() ?: return@withContext false
        
        try {
            // Check for admin role in the profiles table
            val response = postgrest["profiles"]
                .select {
                    filter {
                        eq("user_id", currentUser.id)
                        eq("role", "ADMIN")
                    }
                }
            response.decodeSingleOrNull<Map<String, String>>() != null
        } catch (e: Exception) {
            false
        }
    }
}
