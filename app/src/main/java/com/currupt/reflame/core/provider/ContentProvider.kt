package com.currupt.reflame.core.provider

import com.currupt.reflame.core.model.ContentTitle
import com.currupt.reflame.core.model.Chapter

/**
 * Universal interface for external content providers.
 * Providers should return normalized RΞflame models.
 */
interface ContentProvider {
    val providerId: String
    val providerName: String

    /**
     * Search for content from the provider.
     */
    suspend fun search(query: String): List<ProviderContent>

    /**
     * Get full metadata for a specific content title.
     */
    suspend fun getDetails(externalId: String): ProviderContent?

    /**
     * Get all available chapters for a content title.
     */
    suspend fun getChapters(externalId: String): List<ProviderChapter>
}

/**
 * Normalized content from a provider before ingestion.
 */
data class ProviderContent(
    val externalId: String,
    val title: String,
    val description: String,
    val coverUrl: String = "",
    val backdropUrl: String = "",
    val genres: List<String> = emptyList(),
    val metadata: Map<String, String> = emptyMap()
)

/**
 * Normalized chapter from a provider before ingestion.
 */
data class ProviderChapter(
    val externalId: String,
    val number: Double,
    val title: String? = null,
    val releaseDate: String,
    val metadata: Map<String, String> = emptyMap()
)
