package com.currupt.reflame.core.ingestion

import com.currupt.reflame.core.Supabase
import com.currupt.reflame.core.model.*
import com.currupt.reflame.core.provider.ContentProvider
import com.currupt.reflame.core.provider.ProviderContent
import com.currupt.reflame.core.provider.ProviderChapter
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Service for importing and updating content from providers into Supabase.
 */
class ContentIngestionService {

    private val postgrest = Supabase.client.postgrest

    /**
     * Ingest a content title and all its chapters from a provider.
     * This operation is idempotent.
     */
    suspend fun ingestTitle(provider: ContentProvider, externalId: String, vertical: Vertical, contentType: ContentType) = withContext(Dispatchers.IO) {
        try {
            // 1. Get Provider Details
            val pContent = provider.getDetails(externalId) ?: return@withContext
            
            // 2. Check for existing mapping
            val existingMapping = postgrest["provider_mappings"]
                .select {
                    filter {
                        eq("provider_id", provider.providerId)
                        eq("external_id", externalId)
                        eq("mapping_type", "TITLE")
                    }
                }
                .decodeSingleOrNull<Map<String, String>>()

            val reflameId = if (existingMapping != null) {
                existingMapping["reflame_content_id"]!!
            } else {
                // 3. Create new RΞflame content record
                val newTitle = ContentTitle(
                    id = "", // Supabase will generate UUID
                    title = pContent.title,
                    description = pContent.description,
                    coverUrl = pContent.coverUrl,
                    backdropUrl = pContent.backdropUrl,
                    vertical = vertical,
                    type = contentType,
                    genres = pContent.genres,
                    status = ContentStatus.ONGOING
                )
                val inserted = postgrest["content"].insert(newTitle).decodeSingle<ContentTitle>()
                
                // 4. Create mapping
                val mapping = mapOf(
                    "provider_id" to provider.providerId,
                    "reflame_content_id" to inserted.id,
                    "external_id" to externalId,
                    "mapping_type" to "TITLE"
                )
                postgrest["provider_mappings"].insert(mapping)
                inserted.id
            }

            // 5. Ingest Chapters
            val pChapters = provider.getChapters(externalId)
            ingestChapters(provider, reflameId, pChapters)

        } catch (e: Exception) {
            // Handle error (logging, etc.)
        }
    }

    private suspend fun ingestChapters(provider: ContentProvider, contentId: String, pChapters: List<ProviderChapter>) {
        pChapters.forEach { pChapter ->
            try {
                // Check if mapping exists
                val existing = postgrest["provider_mappings"]
                    .select {
                        filter {
                            eq("provider_id", provider.providerId)
                            eq("external_id", pChapter.externalId)
                            eq("mapping_type", "CHAPTER")
                        }
                    }
                    .decodeSingleOrNull<Map<String, String>>()

                if (existing == null) {
                    // Create chapter
                    val newChapter = Chapter(
                        id = "",
                        contentId = contentId,
                        number = pChapter.number,
                        title = pChapter.title,
                        releaseDate = pChapter.releaseDate
                    )
                    val inserted = postgrest["chapters"].insert(newChapter).decodeSingle<Chapter>()

                    // Create mapping
                    val mapping = mapOf(
                        "provider_id" to provider.providerId,
                        "reflame_chapter_id" to inserted.id,
                        "external_id" to pChapter.externalId,
                        "mapping_type" to "CHAPTER"
                    )
                    postgrest["provider_mappings"].insert(mapping)
                }
            } catch (e: Exception) {
                // Skip failed chapter
            }
        }
    }
}
