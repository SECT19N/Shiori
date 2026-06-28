package com.section.sho.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.section.ori.model.Token
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_auth_prefs")

class TokenManager(private val context: Context) {
    val tokenFlow: Flow<Token?> = context.dataStore.data.map { preferences ->
        val accessToken = preferences[KEY_ACCESS_TOKEN] ?: return@map null
        val refreshToken = preferences[KEY_REFRESH_TOKEN]
        val expiresAt = preferences[KEY_EXPIRES_AT] ?: 0L

        Token(accessToken, refreshToken ?: "", expiresAt)
    }

    suspend fun saveToken(token: Token) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = token.accessToken
            token.refreshToken.let { preferences[KEY_REFRESH_TOKEN] = it }
            preferences[KEY_EXPIRES_AT] = token.expiresAt
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_EXPIRES_AT = longPreferencesKey("expires_at")
    }
}