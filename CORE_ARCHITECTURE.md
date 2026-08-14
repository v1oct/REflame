# RΞflame Core — Universal Data Architecture

This document describes the universal platform architecture for RΞflame. The goal of this architecture is to provide a unified foundation for all content verticals (Reading, Anime, Movies) and platform systems (Economy, Rewards, Identity).

## 1. Modular Separation
The project is organized into `core` and `feature` layers:
- **Core**: Contains universal models, repository interfaces, and platform-wide logic.
- **Feature**: Contains vertical-specific UI and logic (e.g., `reading`, `anime`).

## 2. Universal Content Model
Instead of vertical-specific databases, RΞflame uses a `ContentTitle` abstraction:
- **ContentTitle**: Universal metadata (id, title, genres, artwork).
- **Vertical**: Discriminator for `READING`, `ANIME`, or `MOVIES`.
- **ContentUnit**: Interface for vertical-specific children (Chapters, Episodes, etc.).

## 3. Identity System
- **User**: Core account data (credentials, role, status).
- **Profile**: Public-facing identity, bio, avatar, and customization.
- **UserRole**: Hierarchical permissions (`USER`, `MODERATOR`, `EDITOR`, `ADMIN`, `OWNER`).

## 4. Universal User Activity
- **Library**: Single user library for all vertical content.
- **Progress**: Universal tracking of consumption (percentage, last position).
- **History**: Platform-wide viewing/reading history.

## 5. RΞflame Economy (Coins)
- **Wallet**: User-specific coin balance.
- **CoinTransaction**: Auditable ledger for every coin movement (PURCHASE, SPEND, REWARD).

## 6. Rewards & Gamification
- **RedemptionCode**: Universal code system for rewards (Coins, Badges, Access).
- **Badge**: Collectible profile assets with varying rarity and optional benefits.

## 7. Platform Systems
- **Announcements**: Multi-vertical platform news and alerts.
- **ContentSection**: Admin-configurable editorial rails and grids.
- **Notifications**: System and content-triggered user alerts.

## 8. Integration Architecture
- **Event System**: The platform is designed to emit events (e.g., `ChapterReleased`) that can be consumed by external integrations like Discord webhooks.

## 9. Future Implementation Strategy
- **Client**: Continue using clean Kotlin + Jetpack Compose.
- **Server**: Universal content repository to be established behind clean interfaces.
- **Migration**: Existing Reading mock data will eventually be mapped to the `ContentTitle` model.
