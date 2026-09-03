package com.currupt.reflame.core

import com.currupt.reflame.core.model.*

object MockData {
    val contents = listOf(
        Content(
            id = "1",
            title = "CURRUPT. OS",
            slug = "currupt-os",
            description = "An experimental mobile interface concept exploring non-linear navigation and organic motion. Built with Compose.",
            contentType = ContentType.PROJECT,
            status = ContentStatus.BETA,
            coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1000&auto=format&fit=crop",
            bannerUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1000&auto=format&fit=crop",
            isFeatured = true,
            tags = listOf("Android", "UI/UX", "Experimental")
        ),
        Content(
            id = "2",
            title = "NEON Nexus",
            slug = "neon-nexus",
            description = "A collaborative digital canvas for experimental artists. Connect and create in real-time.",
            contentType = ContentType.PROJECT,
            status = ContentStatus.RELEASED,
            coverUrl = "https://images.unsplash.com/photo-1605142859862-978be7eba909?q=80&w=1000&auto=format&fit=crop",
            bannerUrl = "https://images.unsplash.com/photo-1605142859862-978be7eba909?q=80&w=1000&auto=format&fit=crop",
            isFeatured = true,
            tags = listOf("Web", "Collaboration")
        ),
        Content(
            id = "3",
            title = "Discord Server Template",
            slug = "discord-template-dark",
            description = "A premium, minimal Discord server template designed for creative communities and studios.",
            contentType = ContentType.PRESET,
            status = ContentStatus.RELEASED,
            coverUrl = "https://images.unsplash.com/photo-1614680376593-902f74cc0d41?q=80&w=1000&auto=format&fit=crop",
            isFeatured = false,
            tags = listOf("Discord", "Community")
        ),
        Content(
            id = "4",
            title = "GLITCH Garden",
            slug = "glitch-garden",
            description = "A procedural plant growth simulator where every mutation is a beautiful mistake.",
            contentType = ContentType.EXPERIMENT,
            status = ContentStatus.CONCEPT,
            coverUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?q=80&w=1000&auto=format&fit=crop",
            isFeatured = false,
            tags = listOf("Simulation", "Glitch Art")
        ),
        Content(
            id = "5",
            title = "Studio Showcase 2026",
            slug = "studio-showcase-video",
            description = "A visual journey through the latest projects and experiments from CURRUPT. Studio.",
            contentType = ContentType.MEDIA,
            status = ContentStatus.RELEASED,
            coverUrl = "https://images.unsplash.com/photo-1492619334760-22c0217e33ff?q=80&w=1000&auto=format&fit=crop",
            isFeatured = true,
            media = listOf(
                MediaItem("m1", MediaType.VIDEO, "https://example.com/showcase.mp4", "Full Showcase")
            )
        )
    )

    val sections = listOf(
        StudioSection(
            id = "announcement",
            title = "Studio News",
            type = SectionType.ANNOUNCEMENT,
            priority = 0
        ),
        StudioSection(
            id = "hero",
            title = "Featured",
            type = SectionType.HERO,
            priority = 1
        ),
        StudioSection(
            id = "projects",
            title = "Latest Projects",
            type = SectionType.RAIL,
            priority = 2
        ),
        StudioSection(
            id = "experiments",
            title = "Experiments",
            type = SectionType.RAIL,
            priority = 3
        )
    )
}
