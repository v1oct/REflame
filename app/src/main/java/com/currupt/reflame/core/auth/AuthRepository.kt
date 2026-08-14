package com.currupt.reflame.core.auth

import com.currupt.reflame.core.Supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Authentication state for RΞflame.
 */
sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val user: UserInfo) : AuthState()
    object Unauthenticated : AuthState()
}

/**
 * Repository for managing RΞflame authentication.
 */
class AuthRepository {
    
    private val auth = Supabase.client.auth
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: Flow<AuthState> = _authState.asStateFlow()

    init {
        // Initial session check
        refreshSession()
    }

    suspend fun signUp(email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
        refreshSession()
    }

    suspend fun login(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        refreshSession()
    }

    suspend fun logout() {
        auth.signOut()
        refreshSession()
    }

    private fun refreshSession() {
        val user = auth.currentUserOrNull()
        if (user != null) {
            _authState.value = AuthState.Authenticated(user)
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
}
