package com.currupt.reflame.core.database

import com.currupt.reflame.core.Supabase
import com.currupt.reflame.core.model.*
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Universal content repository for CURRUPT. Studio.
 */
class ContentRepository {
    private val postgrest = Supabase.client.postgrest

    suspend fun getContent(contentType: ContentType? = null, categoryId: String? = null): List<Content> = withContext(Dispatchers.IO) {
        try {
            postgrest["content"].select {
                filter {
                    eq("is_published", true)
                    contentType?.let { eq("content_type", it.name) }
                    categoryId?.let { eq("category_id", it) }
                }
            }.decodeList<Content>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getContentBySlug(slug: String): Content? = withContext(Dispatchers.IO) {
        try {
            postgrest["content"].select {
                filter { eq("slug", slug) }
            }.decodeSingleOrNull<Content>()
        } catch (e: Exception) {
            null
        }
    }
}

class CategoryRepository {
    private val postgrest = Supabase.client.postgrest

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        try {
            postgrest["categories"].select {
                filter { eq("is_active", true) }
            }.decodeList<Category>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

class StudioSectionRepository {
    private val postgrest = Supabase.client.postgrest

    suspend fun getSections(): List<StudioSection> = withContext(Dispatchers.IO) {
        try {
            postgrest["studio_sections"]
                .select { filter { eq("is_visible", true) } }
                .decodeList<StudioSection>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
