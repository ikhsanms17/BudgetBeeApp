package com.budgetbee.app.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")

object SessionManager {
    private val KEY_USER_ID = intPreferencesKey("user_id")
    private val KEY_USER_NAME = stringPreferencesKey("user_name")
    private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")


    suspend fun saveSession(context: Context, id: Int, nama: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = id
            preferences[KEY_USER_NAME] = nama
            preferences[IS_LOGGED_IN] = true

        }
    }

    suspend fun getSession(context: Context): Pair<Int, String>? {
        return context.dataStore.data.map { preferences ->
            val id = preferences[KEY_USER_ID] ?: -1
            val name = preferences[KEY_USER_NAME] ?: ""
            if (id != -1 && name.isNotEmpty()) id to name else null
        }.first()
    }

    suspend fun clearSession(context: Context) {
        context.dataStore.edit {
            it.clear()
        }

    }

    suspend fun isLoggedIn(context: Context): Boolean {
        val value = context.dataStore.data.map { it[IS_LOGGED_IN] == true }.first()
        return value
    }

    suspend fun setLoggedIn(context: Context, status: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = status
        }
    }

    suspend fun getUserName(context: Context): String? {
        return context.dataStore.data.map { it[KEY_USER_NAME] }.first()
    }

    suspend fun getUserEmail(context: Context): String? {
        return context.dataStore.data.map { it[KEY_USER_EMAIL] }.first()
    }
}
