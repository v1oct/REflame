-- RΞflame Supabase Schema & RLS Policies Foundation

-- 1. Profiles Table
CREATE TABLE IF NOT EXISTS public.profiles (
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
    username TEXT UNIQUE,
    display_name TEXT,
    avatar_url TEXT,
    banner_url TEXT,
    bio TEXT,
    active_titles TEXT[] DEFAULT '{}',
    badges TEXT[] DEFAULT '{}',
    stats JSONB DEFAULT '{"titles_read": 0, "chapters_read": 0, "level": 1, "xp": 0}',
    customization JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Public profiles are viewable by everyone." ON public.profiles
    FOR SELECT USING (true);

CREATE POLICY "Users can insert their own profile." ON public.profiles
    FOR INSERT WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can update their own profile." ON public.profiles
    FOR UPDATE USING (auth.uid() = user_id);

-- 2. Universal Content Table
CREATE TABLE IF NOT EXISTS public.content (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    cover_url TEXT,
    backdrop_url TEXT,
    vertical TEXT NOT NULL, -- READING, MUSIC
    content_type TEXT NOT NULL, -- MANHWA, MANGA, SERIES, etc.
    genres TEXT[] DEFAULT '{}',
    status TEXT DEFAULT 'ONGOING',
    is_hot BOOLEAN DEFAULT false,
    is_new BOOLEAN DEFAULT false,
    is_trending BOOLEAN DEFAULT false,
    is_early_access BOOLEAN DEFAULT false,
    artwork_color TEXT DEFAULT '#2C3E50',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.content ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Content is viewable by everyone." ON public.content
    FOR SELECT USING (true);

-- 3. Chapters Table
CREATE TABLE IF NOT EXISTS public.chapters (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    content_id UUID REFERENCES public.content(id) ON DELETE CASCADE NOT NULL,
    number DOUBLE PRECISION NOT NULL,
    title TEXT,
    release_date TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    access_state TEXT DEFAULT 'AVAILABLE', -- AVAILABLE, NEW, EARLY_ACCESS, LOCKED
    coin_price INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.chapters ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Chapters are viewable by everyone." ON public.chapters
    FOR SELECT USING (true);

-- 4. Content Collections
CREATE TABLE IF NOT EXISTS public.collections (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    artwork_url TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.collections ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Collections are viewable by everyone." ON public.collections
    FOR SELECT USING (true);

-- 5. Collection Items
CREATE TABLE IF NOT EXISTS public.collection_items (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    collection_id UUID REFERENCES public.collections(id) ON DELETE CASCADE NOT NULL,
    content_id UUID REFERENCES public.content(id) ON DELETE CASCADE NOT NULL,
    priority INTEGER DEFAULT 0,
    UNIQUE(collection_id, content_id)
);

ALTER TABLE public.collection_items ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Collection items are viewable by everyone." ON public.collection_items
    FOR SELECT USING (true);

-- 6. Content Sections (Editorial Home)
CREATE TABLE IF NOT EXISTS public.content_sections (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    subtitle TEXT,
    vertical TEXT, -- READING, MUSIC (Null = Universal)
    type TEXT NOT NULL DEFAULT 'RAIL', -- HERO, RAIL, GRID, BANNER
    source_type TEXT NOT NULL DEFAULT 'LATEST_RELEASES', -- HOT, NEW, TRENDING, COLLECTION, PERSONALIZED
    collection_id UUID REFERENCES public.collections(id),
    priority INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    starts_at BIGINT, -- epoch millis
    expires_at BIGINT, -- epoch millis
    limit_count INTEGER DEFAULT 10,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.content_sections ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Content sections are viewable by everyone." ON public.content_sections
    FOR SELECT USING (true);

-- 7. Library Entries
CREATE TABLE IF NOT EXISTS public.library_entries (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL,
    content_id UUID REFERENCES public.content(id) ON DELETE CASCADE NOT NULL,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    is_favorite BOOLEAN DEFAULT false,
    UNIQUE(user_id, content_id)
);

ALTER TABLE public.library_entries ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their own library entries." ON public.library_entries
    FOR ALL USING (auth.uid() = user_id);

-- 8. Content Progress
CREATE TABLE IF NOT EXISTS public.content_progress (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL,
    content_id UUID REFERENCES public.content(id) ON DELETE CASCADE NOT NULL,
    unit_id TEXT NOT NULL, -- Chapter ID, Episode ID
    percentage INTEGER DEFAULT 0,
    last_position TEXT,
    completed BOOLEAN DEFAULT false,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    UNIQUE(user_id, content_id, unit_id)
);

ALTER TABLE public.content_progress ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their own progress." ON public.content_progress
    FOR ALL USING (auth.uid() = user_id);

-- 9. Wallet System
CREATE TABLE IF NOT EXISTS public.wallets (
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
    balance INTEGER DEFAULT 0 NOT NULL CHECK (balance >= 0),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.wallets ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view their own wallet." ON public.wallets
    FOR SELECT USING (auth.uid() = user_id);

-- 10. Coin Transactions
CREATE TABLE IF NOT EXISTS public.coin_transactions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL,
    type TEXT NOT NULL, -- PURCHASE, REWARD, REDEMPTION, SPEND
    amount INTEGER NOT NULL,
    source TEXT NOT NULL,
    reference_id TEXT,
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.coin_transactions ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view their own transactions." ON public.coin_transactions
    FOR SELECT USING (auth.uid() = user_id);

-- 11. Redemption Codes
CREATE TABLE IF NOT EXISTS public.redemption_codes (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code TEXT UNIQUE NOT NULL,
    reward_type TEXT NOT NULL,
    reward_data JSONB NOT NULL,
    max_uses INTEGER DEFAULT 1,
    current_uses INTEGER DEFAULT 0,
    starts_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN DEFAULT true
);

ALTER TABLE public.redemption_codes ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Redemption codes are viewable by authenticated users." ON public.redemption_codes
    FOR SELECT USING (auth.role() = 'authenticated');

-- 12. Code Redemptions
CREATE TABLE IF NOT EXISTS public.code_redemptions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code_id UUID REFERENCES public.redemption_codes(id) ON DELETE CASCADE NOT NULL,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL,
    redeemed_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    UNIQUE(user_id, code_id)
);

ALTER TABLE public.code_redemptions ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view their own redemptions." ON public.code_redemptions
    FOR SELECT USING (auth.uid() = user_id);

-- 13. Badges
CREATE TABLE IF NOT EXISTS public.badges (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    icon_url TEXT,
    rarity TEXT DEFAULT 'COMMON',
    price INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    benefits JSONB DEFAULT '{}'
);

ALTER TABLE public.badges ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Badges are viewable by everyone." ON public.badges
    FOR SELECT USING (true);

-- 14. User Badges
CREATE TABLE IF NOT EXISTS public.user_badges (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL,
    badge_id UUID REFERENCES public.badges(id) ON DELETE CASCADE NOT NULL,
    acquired_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    source TEXT,
    UNIQUE(user_id, badge_id)
);

ALTER TABLE public.user_badges ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view their own badges." ON public.user_badges
    FOR SELECT USING (auth.uid() = user_id);

-- 15. Announcements
CREATE TABLE IF NOT EXISTS public.announcements (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title TEXT NOT NULL,
    message TEXT,
    type TEXT DEFAULT 'NEWS',
    priority INTEGER DEFAULT 0,
    image_url TEXT,
    action_url TEXT,
    starts_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN DEFAULT true
);

ALTER TABLE public.announcements ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Announcements are viewable by everyone." ON public.announcements
    FOR SELECT USING (true);

-- 16. Notifications
CREATE TABLE IF NOT EXISTS public.notifications (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL,
    type TEXT NOT NULL,
    title TEXT NOT NULL,
    message TEXT,
    reference_id TEXT,
    is_read BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.notifications ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their own notifications." ON public.notifications
    FOR ALL USING (auth.uid() = user_id);
