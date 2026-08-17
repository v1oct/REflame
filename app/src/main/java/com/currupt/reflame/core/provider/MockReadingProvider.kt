package com.currupt.reflame.core.provider

/**
 * A mock authorized content provider for testing the ingestion pipeline.
 * This represents a metadata-only integration with an official partner.
 */
class MockReadingProvider : ContentProvider {
    override val providerId: String = "partner_official_01"
    override val providerName: String = "RΞflame Official Partner"

    override suspend fun search(query: String): List<ProviderContent> {
        return listOf(
            ProviderContent(
                externalId = "ext_101",
                title = "Chronicles of the Dark Flame",
                description = "An official story of a hero rising from the ashes.",
                genres = listOf("Action", "Supernatural")
            )
        )
    }

    override suspend fun getDetails(externalId: String): ProviderContent? {
        if (externalId == "ext_101") {
            return ProviderContent(
                externalId = "ext_101",
                title = "Chronicles of the Dark Flame",
                description = "Full official description for the series.",
                genres = listOf("Action", "Supernatural", "Fantasy")
            )
        }
        return null
    }

    override suspend fun getChapters(externalId: String): List<ProviderChapter> {
        return listOf(
            ProviderChapter(
                externalId = "ch_ext_001",
                number = 1.0,
                title = "Prologue",
                releaseDate = "2026-08-10T12:00:00Z"
            ),
            ProviderChapter(
                externalId = "ch_ext_002",
                number = 2.0,
                title = "The Awakening",
                releaseDate = "2026-08-17T12:00:00Z"
            )
        )
    }
}
