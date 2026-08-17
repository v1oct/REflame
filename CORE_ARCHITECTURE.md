# RΞflame Core — Universal Content-Driven Architecture

This document describes the modular architecture of RΞflame.

## 1. Modular Separation
The project is physically organized into:
- **Core Layer**: Universal models, repository interfaces, and platform-wide logic.
- **Feature Layer**: Vertical-specific implementations (e.g., `Reading`, `Music`).
- **UI Layer**: Reusable components and theme definitions.

## 2. Universal Content System [IMPLEMENTED]
Decouples screen logic from specific content types.
- **ContentTitle**: Universal metadata (id, title, genres, artwork).
- **Vertical**: Active verticals: `READING`, `MUSIC`.
- **ContentUnit**: Interface for children (Chapters).
- **Collections**: Support for grouping content (e.g., "Summer Event").

## 3. Data / Repository Layer [IMPLEMENTED]
Repositories are responsible for data retrieval from Supabase.
- **ContentRepository**: `core/content/ContentRepository.kt`
- **SectionRepository**: `core/content/SectionRepository.kt`
- **ReadingRepository**: `feature/reading/ReadingRepository.kt`
- **MusicRepository**: `feature/music/MusicRepository.kt`
- **CollectionRepository**: `core/collections/CollectionRepository.kt`
- **ProfileRepository**: `core/database/ProfileRepository.kt`

## 4. Feature Modules [IMPLEMENTED]
- **Reading**: 
    - `feature/reading/ReadingHomeViewModel.kt`
    - `feature/reading/TitleDetailsViewModel.kt`
    - Vertical scrolling chapter reader.
- **Music**:
    - `feature/music/MusicHomeViewModel.kt`
    - Support for Tracks, Albums, Artists (Foundation only).

## 5. Data-Driven Editorial [IMPLEMENTED]
Home screens are rendered dynamically using a section-based architecture.
- **ContentSection**: Defines layout (HERO, RAIL), source (HOT, NEW, COLLECTION), and availability.
- **SectionRenderer**: Reusable logic in `ReadingScreen.kt` for mapping data to UI.

## 6. Content Ingestion & Providers [IMPLEMENTED]
Established a provider-agnostic pipeline for authorized content:
- **ContentProvider**: Interface for external metadata and content sources.
- **Provider Mapping**: Securely links RΞflame IDs to external provider IDs in Supabase.
- **IngestionService**: Idempotent service to normalize and import content/chapters.
- **Copyright Safety**: Architecture strictly supports authorized/licensed sources.

## 7. Shared Platform Systems [IMPLEMENTED]
- **Identity**: `core/auth/AuthRepository.kt`, `core/model/Identity.kt`.
- **Economy**: `core/model/Economy.kt` (RΞflame Coins).
- **Availability**: `core/availability/AvailabilityService.kt`.
- **Rewards**: `core/model/Rewards.kt` (Codes, Badges).
- **Activity**: `core/model/Activity.kt` (Library, Progress, History).

## 7. Security (Supabase)
- **RLS**: PostgreSQL policies ensure data privacy. Users can only manage their own Library and Progress.
- **Stateless UI**: UI components consume data via ViewModels.
