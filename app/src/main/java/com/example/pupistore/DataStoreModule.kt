package com.example.pupistore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")
val THEME_KEY = booleanPreferencesKey("dark_mode_enabled")

suspend fun saveTheme(isDark: Boolean, context: Context) {
    context.dataStore.edit { prefs ->
        prefs[THEME_KEY] = isDark
    }
}

fun getTheme(context: Context): Flow<Boolean> {
    return context.dataStore.data.map { prefs ->
        prefs[THEME_KEY] ?: false
    }
}
