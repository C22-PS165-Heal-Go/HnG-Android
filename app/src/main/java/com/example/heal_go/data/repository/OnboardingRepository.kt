package com.example.heal_go.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")

class OnboardingRepository(private val context: Context) {

    private val KEY = booleanPreferencesKey("onboarding")

    suspend fun saveToDataStore(isFinished: Boolean) {
        context.dataStore.edit { preference ->
            preference[KEY] = isFinished
        }
    }

    val readFromDataStore: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("Onboarding Datastore", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val isFinised = preference[KEY] ?: false
            isFinised
        }

}