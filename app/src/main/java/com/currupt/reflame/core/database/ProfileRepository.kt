package com.currupt.reflame.core.database

import com.currupt.reflame.core.Supabase
import com.currupt.reflame.core.model.Profile
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

/**
 * Repository for managing RΞflame user profiles in Supabase.
 */
class ProfileRepository {
    
    private val postgrest = Supabase.client.postgrest

    suspend fun getProfile(userId: String): Profile? = withContext(Dispatchers.IO) {
        try {
            val response = postgrest["profiles"]
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
            response.decodeSingleOrNull<Profile>()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createProfile(profile: Profile) = withContext(Dispatchers.IO) {
        postgrest["profiles"].insert(profile)
    }

    suspend fun updateProfile(profile: Profile) = withContext(Dispatchers.IO) {
        postgrest["profiles"].update(profile) {
            filter {
                eq("user_id", profile.userId)
            }
        }
    }
}
