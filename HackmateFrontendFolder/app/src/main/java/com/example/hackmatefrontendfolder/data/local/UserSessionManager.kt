package com.example.hackmatefrontendfolder.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("user_prefs")

@Singleton
class UserSessionManager @Inject constructor(
    @ApplicationContext private val context : Context,
) {

    companion object{
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val EMAIL_VERIFIED_KEY = booleanPreferencesKey("email_verified")
        private val PROFILE_SETUP_KEY = booleanPreferencesKey("profile_setup")
    }

    suspend fun saveToken(token : String){
        context.dataStore.edit{prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    val token : Flow<String?> = context.dataStore.data
        .map{ prefs ->
            prefs[TOKEN_KEY]
        }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
        }
    }

    val email: Flow<String?> = context.dataStore.data
        .map { prefs ->
            prefs[EMAIL_KEY]
        }

    suspend fun saveEmailVerified(verified: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL_VERIFIED_KEY] = verified
        }
    }

    val isEmailVerified: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[EMAIL_VERIFIED_KEY] ?: false
        }

    suspend fun saveProfileSetup(isSetup: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PROFILE_SETUP_KEY] = isSetup
        }
    }

    val isProfileSetup: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[PROFILE_SETUP_KEY] ?: false
        }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}