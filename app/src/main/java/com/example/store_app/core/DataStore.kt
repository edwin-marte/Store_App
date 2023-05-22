package com.example.store_app.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")
    }

    fun getAccessToken(USER_TOKEN_KEY: Preferences.Key<String>): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_TOKEN_KEY] ?: ""
    }

    suspend fun clearDataStore(USER_TOKEN_KEY: Preferences.Key<String>): Resource<Preferences> {
        return Resource.Success(context.dataStore.edit {
            it.remove(USER_TOKEN_KEY)
        })
    }

    suspend fun saveToken(token: String, USER_TOKEN_KEY: Preferences.Key<String>) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }
}