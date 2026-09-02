# CURRUPT. Studio — Project Architecture

This document describes the evolved modular architecture of the CURRUPT. Studio app.

## 1. Modular Separation
The project is physically organized into:
- **Core Layer**: Universal models, repository interfaces, and platform-wide logic.
- **UI Layer**: Reusable components and theme definitions.

## 2. Studio Domain Models [IMPLEMENTED]
Models representing the core studio entities.
- **Project**: Represents a GAME, APP, or EXPERIMENT. Includes metadata, status, artwork, and links.
- **Announcement**: Studio news and updates.
- **Release**: Versioned releases of projects.
- **DevelopmentLog**: Progress logs for projects in development.
- **StudioSection**: Data-driven homepage sections (FEATURED, RECENT_RELEASES, etc.).

## 3. Data / Repository Layer [IMPLEMENTED]
Repositories responsible for retrieving data from Supabase.
- **ProjectRepository**: `core/database/StudioRepositories.kt`
- **AnnouncementRepository**: `core/database/StudioRepositories.kt`
- **ReleaseRepository**: `core/database/StudioRepositories.kt`
- **DevelopmentLogRepository**: `core/database/StudioRepositories.kt`
- **StudioSectionRepository**: `core/database/StudioRepositories.kt`
- **ProfileRepository**: `core/database/ProfileRepository.kt`
- **AdminRepository**: `core/auth/AdminRepository.kt`

## 4. Identity & Admin Foundation [IMPLEMENTED]
- **Identity**: `core/auth/AuthRepository.kt`, `core/model/Identity.kt`.
- **Admin Authorization**: Server-authoritative check for admin roles (ADMIN, USER, VISITOR).

## 5. Security (Supabase)
- **RLS**: PostgreSQL policies ensure data privacy. Users can only manage their own profiles. Admins have full access to studio content.
- **Public Access**: Visitors can read all published studio content without logging in.

## 6. Future Implementation Strategy
- **Admin Dashboard**: A separate area for authorized accounts to manage projects and announcements.
- **Media Engine**: Immersive project details with hero videos and artwork.
