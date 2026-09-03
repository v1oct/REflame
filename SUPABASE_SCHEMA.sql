-- CURRUPT. Studio Supabase Schema — Content Canvas Architecture

-- 1. Profiles & Admin Roles
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

CREATE POLICY "Public profiles are viewable by everyone." ON public.profiles FOR SELECT USING (true);
CREATE POLICY "Users can update their own profile." ON public.profiles FOR UPDATE USING (auth.uid() = user_id);

-- 2. Categories
CREATE TABLE IF NOT EXISTS public.categories (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    slug TEXT UNIQUE NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.categories ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Categories are viewable by everyone." ON public.categories FOR SELECT USING (true);

-- 3. Content
CREATE TABLE IF NOT EXISTS public.content (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    slug TEXT UNIQUE NOT NULL,
    description TEXT,
    category_id UUID REFERENCES public.categories(id),
    content_type TEXT NOT NULL, -- PROJECT, PRESET, TEMPLATE, ARTICLE, MEDIA, RELEASE, ANNOUNCEMENT
    status TEXT DEFAULT 'RELEASED', -- CONCEPT, IN_DEVELOPMENT, BETA, RELEASED, ARCHIVED
    cover_url TEXT,
    banner_url TEXT,
    is_featured BOOLEAN DEFAULT false,
    is_published BOOLEAN DEFAULT true,
    tags TEXT[] DEFAULT '{}',
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.content ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Published content is viewable by everyone." ON public.content
    FOR SELECT USING (is_published = true);

CREATE POLICY "Admins can manage content." ON public.content
    FOR ALL USING (EXISTS (SELECT 1 FROM public.profiles WHERE user_id = auth.uid() AND role = 'ADMIN'));

-- 4. Content Media
CREATE TABLE IF NOT EXISTS public.content_media (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    content_id UUID REFERENCES public.content(id) ON DELETE CASCADE NOT NULL,
    type TEXT NOT NULL, -- IMAGE, VIDEO, AUDIO, GALLERY, EXTERNAL_LINK
    url TEXT NOT NULL,
    title TEXT,
    thumbnail_url TEXT,
    duration BIGINT,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.content_media ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Media is viewable by everyone." ON public.content_media FOR SELECT USING (true);

-- 5. Studio Sections (Dynamic Home)
CREATE TABLE IF NOT EXISTS public.studio_sections (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    subtitle TEXT,
    type TEXT NOT NULL, -- HERO, RAIL, GRID, FEATURED, ANNOUNCEMENT, MEDIA, TEXT
    priority INTEGER DEFAULT 0,
    is_visible BOOLEAN DEFAULT true,
    query_config JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.studio_sections ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Visible sections are viewable by everyone." ON public.studio_sections
    FOR SELECT USING (is_visible = true);
