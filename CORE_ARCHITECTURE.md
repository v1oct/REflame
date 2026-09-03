# CURRUPT. Studio — Content Canvas Architecture

This document describes the modular architecture of the CURRUPT. Studio app, now fully genericized into a flexible content platform.

## 1. Modular Separation
- **Core Layer**: Generic content models, data-driven repository interfaces, and media engine.
- **UI Layer**: Reusable components, theme definitions, and data-driven screen renderers.

## 2. Generic Content Model [IMPLEMENTED]
CURRUPT. uses a unified `Content` model capable of representing:
- **PROJECTS**: Apps, Digital Projects, Experiments.
- **PRESETS / TEMPLATES**: Discord templates, UI kits, etc.
- **MEDIA**: Videos, Studio Showcases, Audio.
- **ARTICLES**: Studio updates, dev logs.
- **RELEASES & ANNOUNCEMENTS**.

## 3. Media Architecture [IMPLEMENTED]
- **MediaManager**: Reusable foundation for Audio and Video playback using **Jetpack Media3 (ExoPlayer)**.
- **ContentMedia**: Generic attachment model for IMAGE, VIDEO, AUDIO, and GALLERY items.

## 4. Data-Driven Homepage (Canvas) [IMPLEMENTED]
The homepage is no longer hardcoded. It renders a sequence of `StudioSection` items:
- **HERO**: Cinematic featured content.
- **RAIL**: Horizontal scrolling discovery rails.
- **ANNOUNCEMENT**: Integrated studio notices.
- **TEXT**: Editorial descriptions.

## 5. Repositories [IMPLEMENTED]
- **ContentRepository**: Universal retrieval by type, category, or slug.
- **CategoryRepository**: Management of data-driven studio categories.
- **StudioSectionRepository**: Fetching of homepage layouts.
- **MediaRepository**: (Foundation) Reusable media item management.

## 6. Security (Supabase)
- **RLS**: Policies ensure that only published content is viewable by visitors.
- **Admin**: Auth rules allow authorized accounts to manage the entire canvas.

## 7. Future Directions
- **Interactive Player UI**: Custom skins for CURRUPT. Audio and Video players.
- **Admin CMS**: A secure area for real-time content and section management.
