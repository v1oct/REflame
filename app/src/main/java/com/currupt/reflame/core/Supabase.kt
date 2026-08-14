package com.currupt.reflame.core

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

/**
 * Centralized Supabase client provider for RΞflame.
 * 
 * IMPORTANT: Replace the placeholders below with your real Supabase project credentials.
 * Get these from your Supabase Dashboard -> Settings -> API.
 */
object Supabase {
    
    // Replace with your project URL (e.g. "https://xyz.supabase.co")
    const val PROJECT_URL = "YOUR_SUPABASE_URL"
    
    // Replace with your public/anon key
    const val ANON_KEY = "YOUR_SUPABASE_ANON_KEY"

    val client = createSupabaseClient(
        supabaseUrl = PROJECT_URL,
        supabaseKey = ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
