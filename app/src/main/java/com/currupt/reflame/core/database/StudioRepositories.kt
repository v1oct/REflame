package com.currupt.reflame.core.database

import com.currupt.reflame.core.Supabase
import com.currupt.reflame.core.model.*
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectRepository {
    private val postgrest = Supabase.client.postgrest

    suspend fun getProjects(): List<Project> = withContext(Dispatchers.IO) {
        try {
            postgrest["projects"].select().decodeList<Project>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getProjectBySlug(slug: String): Project? = withContext(Dispatchers.IO) {
        try {
            postgrest["projects"].select {
                filter { eq("slug", slug) }
            }.decodeSingleOrNull<Project>()
        } catch (e: Exception) {
            null
        }
    }
}

class AnnouncementRepository {
    private val postgrest = Supabase.client.postgrest

    suspend fun getAnnouncements(): List<Announcement> = withContext(Dispatchers.IO) {
        try {
            postgrest["announcements"]
                .select { filter { eq("is_published", true) } }
                .decodeList<Announcement>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

class ReleaseRepository {
    private val postgrest = Supabase.client.postgrest

    suspend fun getReleases(): List<Release> = withContext(Dispatchers.IO) {
        try {
            postgrest["releases"].select().decodeList<Release>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

class DevelopmentLogRepository {
    private val postgrest = Supabase.client.postgrest

    suspend fun getLogs(projectId: String): List<DevelopmentLog> = withContext(Dispatchers.IO) {
        try {
            postgrest["development_logs"].select {
                filter { eq("project_id", projectId) }
            }.decodeList<DevelopmentLog>()
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
