package com.example.heal_go.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.heal_go.data.repository.OnboardingRepository.Companion.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class RecommendationTutorialRepository(private val context: Context) {
    private val KEY = booleanPreferencesKey("tutorial")

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