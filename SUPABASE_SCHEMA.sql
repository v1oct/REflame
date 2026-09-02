-- CURRUPT. Studio Supabase Schema

-- 1. Profiles & Admin Roles
-- Profiles are public, but sensitive data is restricted by RLS.
CREATE TABLE IF NOT EXISTS public.profiles (
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
    username TEXT UNIQUE,
    display_name TEXT,
    avatar_url TEXT,
    banner_url TEXT,
    bio TEXT,
    role TEXT DEFAULT 'VISITOR', -- VISITOR, USER, ADMIN
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Public profiles are viewable by everyone." ON public.profiles
    FOR SELECT USING (true);

CREATE POLICY "Users can update their own profile." ON public.profiles
    FOR UPDATE USING (auth.uid() = user_id);

-- 2. Projects
CREATE TABLE IF NOT EXISTS public.projects (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    slug TEXT UNIQUE NOT NULL,
    description TEXT,
    type TEXT NOT NULL, -- GAME, APP, EXPERIMENT
    status TEXT NOT NULL, -- CONCEPT, IN_DEVELOPMENT, BETA, RELEASED, ARCHIVED
    cover_url TEXT,
    hero_url TEXT,
    hero_video_url TEXT,
    progress INTEGER DEFAULT 0,
    is_featured BOOLEAN DEFAULT false,
    release_info TEXT,
    external_links JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.projects ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Projects are viewable by everyone." ON public.projects
    FOR SELECT USING (true);

CREATE POLICY "Admins can manage projects." ON public.projects
    FOR ALL USING (
        EXISTS (
            SELECT 1 FROM public.profiles
            WHERE user_id = auth.uid() AND role = 'ADMIN'
        )
    );

-- 3. Announcements
CREATE TABLE IF NOT EXISTS public.announcements (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    type TEXT, -- NEWS, UPDATE, EVENT
    artwork_url TEXT,
    is_published BOOLEAN DEFAULT false,
    published_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.announcements ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Published announcements are viewable by everyone." ON public.announcements
    FOR SELECT USING (is_published = true);

CREATE POLICY "Admins can manage announcements." ON public.announcements
    FOR ALL USING (
        EXISTS (
            SELECT 1 FROM public.profiles
            WHERE user_id = auth.uid() AND role = 'ADMIN'
        )
    );

-- 4. Releases
CREATE TABLE IF NOT EXISTS public.releases (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    project_id UUID REFERENCES public.projects(id) ON DELETE CASCADE NOT NULL,
    version TEXT NOT NULL,
    title TEXT,
    description TEXT,
    release_date TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    is_published BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.releases ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Published releases are viewable by everyone." ON public.releases
    FOR SELECT USING (is_published = true);

CREATE POLICY "Admins can manage releases." ON public.releases
    FOR ALL USING (
        EXISTS (
            SELECT 1 FROM public.profiles
            WHERE user_id = auth.uid() AND role = 'ADMIN'
        )
    );

-- 5. Development Logs
CREATE TABLE IF NOT EXISTS public.development_logs (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    project_id UUID REFERENCES public.projects(id) ON DELETE CASCADE NOT NULL,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    is_published BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.development_logs ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Published dev logs are viewable by everyone." ON public.development_logs
    FOR SELECT USING (is_published = true);

CREATE POLICY "Admins can manage dev logs." ON public.development_logs
    FOR ALL USING (
        EXISTS (
            SELECT 1 FROM public.profiles
            WHERE user_id = auth.uid() AND role = 'ADMIN'
        )
    );

-- 6. Studio Sections (Dynamic Home)
CREATE TABLE IF NOT EXISTS public.studio_sections (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    subtitle TEXT,
    type TEXT NOT NULL, -- FEATURED, RECENT_RELEASES, GAMES, APPS, etc.
    priority INTEGER DEFAULT 0,
    is_visible BOOLEAN DEFAULT true,
    availability_starts_at BIGINT,
    availability_expires_at BIGINT,
    query_config JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.studio_sections ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Visible sections are viewable by everyone." ON public.studio_sections
    FOR SELECT USING (is_visible = true);

CREATE POLICY "Admins can manage studio sections." ON public.studio_sections
    FOR ALL USING (
        EXISTS (
            SELECT 1 FROM public.profiles
            WHERE user_id = auth.uid() AND role = 'ADMIN'
        )
    );
