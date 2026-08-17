package com.currupt.reflame.core.collections

import com.currupt.reflame.core.Supabase
import com.currupt.reflame.core.model.ContentCollection
import com.currupt.reflame.core.model.ContentTitle
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Universal repository for managing content collections.
 */
interface CollectionRepository {
    suspend fun getCollection(id: String): ContentCollection?
    suspend fun getCollectionTitles(collectionId: String): List<ContentTitle>
}

class SupabaseCollectionRepository : CollectionRepository {

    private val postgrest = Supabase.client.postgrest

    override suspend fun getCollection(id: String): ContentCollection? = withContext(Dispatchers.IO) {
        try {
            postgrest["collections"]
                .select {
                    filter {
                        eq("id", id)
                        eq("is_active", true)
                    }
                }
                .decodeSingleOrNull<ContentCollection>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getCollectionTitles(collectionId: String): List<ContentTitle> = withContext(Dispatchers.IO) {
        try {
            // Fetch content IDs from collection_items
            val itemIds = postgrest["collection_items"]
                .select {
                    filter {
                        eq("collection_id", collectionId)
                    }
                }
                .decodeList<Map<String, String>>()
                .mapNotNull { it["content_id"] }

            if (itemIds.isEmpty()) return@withContext emptyList()

            // Fetch actual content records
            postgrest["content"]
                .select {
                    filter {
                        isIn("id", itemIds)
                    }
                }
                .decodeList<ContentTitle>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
