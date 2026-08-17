package com.currupt.reflame.core.content

import com.currupt.reflame.core.Supabase
import com.currupt.reflame.core.model.ContentSection
import com.currupt.reflame.core.model.Vertical
import com.currupt.reflame.core.availability.AvailabilityService
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Universal repository for retrieving content sections.
 */
interface SectionRepository {
    suspend fun getSections(vertical: Vertical): List<ContentSection>
}

class SupabaseSectionRepository : SectionRepository {

    private val postgrest = Supabase.client.postgrest

    override suspend fun getSections(vertical: Vertical): List<ContentSection> = withContext(Dispatchers.IO) {
        try {
            val sections = postgrest["content_sections"]
                .select {
                    filter {
                        or {
                            eq("vertical", vertical.name)
                            // Supabase kt doesn't easily support 'is null' in or block via simple helper sometimes
                            // Use raw filter or exact eq if mapped differently. 
                            // For now, let's assume vertical is provided.
                        }
                        eq("is_active", true)
                    }
                    order("priority", Order.ASCENDING)
                }
                .decodeList<ContentSection>()

            // Filter by availability
            sections.filter { section ->
                AvailabilityService.isAvailable(
                    startsAt = section.startsAt,
                    expiresAt = section.expiresAt,
                    isActive = section.isActive
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
