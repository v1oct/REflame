package com.currupt.reflame.core.content

import com.currupt.reflame.core.Supabase
import com.currupt.reflame.core.model.*
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Universal repository for retrieving RΞflame content.
 */
interface ContentRepository {
    suspend fun getTitles(vertical: Vertical): List<ContentTitle>
    suspend fun getTitleDetails(id: String): ContentTitle?
    suspend fun getChapters(titleId: String): List<Chapter>
    suspend fun getTitlesForSection(section: ContentSection): List<ContentTitle>
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
                    order("number", Order.DESCENDING)
                }
                .decodeList<Chapter>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTitlesForSection(section: ContentSection): List<ContentTitle> = withContext(Dispatchers.IO) {
        try {
            val vertical = section.vertical ?: Vertical.READING
            
            val query = postgrest["content"].select {
                filter {
                    eq("vertical", vertical.name)
                    
                    when (section.sourceType) {
                        SectionSourceType.HOT -> eq("is_hot", true)
                        SectionSourceType.NEW -> eq("is_new", true)
                        SectionSourceType.TRENDING -> eq("is_trending", true)
                        SectionSourceType.LATEST_RELEASES -> {} // Default vertical filter
                        SectionSourceType.COLLECTION -> {
                            // This would ideally be a join or subquery. 
                            // For now, let's assume we fetch by IDs if we had them, 
                            // or just use a placeholder if collectionId is present.
                        }
                        SectionSourceType.PERSONALIZED -> {
                            // Personalized logic (History/Library)
                        }
                    }
                }
                limit(section.limit.toLong())
            }
            
            query.decodeList<ContentTitle>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
