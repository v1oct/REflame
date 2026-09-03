package com.currupt.reflame.core

import com.currupt.reflame.core.model.*

object MockData {
    val projects = listOf(
        Project(
            id = "1",
            title = "AETHER: Remastered",
            slug = "aether-remastered",
            description = "A high-octane experimental racing game set in a dystopian future where gravity is optional.",
            type = ProjectType.GAME,
            status = ProjectStatus.IN_DEVELOPMENT,
            coverUrl = "https://images.unsplash.com/photo-1614732414444-096e5f1122d5?q=80&w=1000&auto=format&fit=crop",
            heroUrl = "https://images.unsplash.com/photo-1614732414444-096e5f1122d5?q=80&w=1000&auto=format&fit=crop",
            progress = 65,
            isFeatured = true,
            releaseInfo = "Target Q4 2026"
        ),
        Project(
            id = "2",
            title = "CURRUPT. OS",
            slug = "currupt-os",
            description = "An experimental mobile interface concept exploring non-linear navigation and organic motion.",
            type = ProjectType.APP,
            status = ProjectStatus.BETA,
            coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1000&auto=format&fit=crop",
            heroUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1000&auto=format&fit=crop",
            progress = 80,
            isFeatured = true,
            releaseInfo = "Early Access available"
        ),
        Project(
            id = "3",
            title = "GLITCH Garden",
            slug = "glitch-garden",
            description = "A procedural plant growth simulator where every mutation is a beautiful mistake.",
            type = ProjectType.EXPERIMENT,
            status = ProjectStatus.CONCEPT,
            coverUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?q=80&w=1000&auto=format&fit=crop",
            heroUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?q=80&w=1000&auto=format&fit=crop",
            progress = 20,
            isFeatured = false
        ),
        Project(
            id = "4",
            title = "NEON Nexus",
            slug = "neon-nexus",
            description = "A collaborative digital canvas for experimental artists.",
            type = ProjectType.APP,
            status = ProjectStatus.RELEASED,
            coverUrl = "https://images.unsplash.com/photo-1605142859862-978be7eba909?q=80&w=1000&auto=format&fit=crop",
            heroUrl = "https://images.unsplash.com/photo-1605142859862-978be7eba909?q=80&w=1000&auto=format&fit=crop",
            progress = 100,
            isFeatured = false,
            releaseInfo = "Version 1.0.4"
        )
    )

    val announcements = listOf(
        Announcement(
            id = "1",
            title = "Something new is coming.",
            description = "We are preparing for the first CURRUPT. showcase. Stay tuned for AETHER: Remastered gameplay.",
            type = "NEWS",
            isPublished = true,
            publishedAt = "2026-09-01T12:00:00Z"
        )
    )

    val sections = listOf(
        StudioSection(
            id = "hero",
            title = "Featured",
            type = SectionType.FEATURED,
            priority = 0
        ),
        StudioSection(
            id = "dev",
            title = "In Development",
            type = SectionType.IN_DEVELOPMENT,
            priority = 1
        ),
        StudioSection(
            id = "games",
            title = "Games",
            type = SectionType.GAMES,
            priority = 2
        ),
        StudioSection(
            id = "apps",
            title = "Apps",
            type = SectionType.APPS,
            priority = 3
        ),
        StudioSection(
            id = "experiments",
            title = "Experiments",
            type = SectionType.EXPERIMENTS,
            priority = 4
        )
    )
}
