package com.currupt.reflame.core.content

import com.currupt.reflame.core.Supabase
import com.currupt.reflame.core.model.*
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Universal repository for retrieving RΞflame content.
 */
interface ContentRepository {
    suspend fun getTitles(vertical: Vertical): List<ContentTitle>
    suspend fun getTitleDetails(id: String): ContentTitle?
    suspend fun getChapters(titleId: String): List<Chapter>
}

class SupabaseContentRepository : ContentRepository {

    private val postgrest = Supabase.client.postgrest

    override suspend fun getTitles(vertical: Vertical): List<ContentTitle> = withContext(Dispatchers.IO) {
        try {
            postgrest["content"]
                .select {
                    filter {
                        eq("vertical", vertical.name)
                    }
                }
                .decodeList<ContentTitle>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTitleDetails(id: String): ContentTitle? = withContext(Dispatchers.IO) {
        try {
            postgrest["content"]
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingleOrNull<ContentTitle>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getChapters(titleId: String): List<Chapter> = withContext(Dispatchers.IO) {
        try {
            postgrest["chapters"]
                .select {
                    filter {
                        eq("content_id", titleId)
                    }
                }
                .decodeList<Chapter>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
