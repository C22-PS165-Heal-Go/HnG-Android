package com.example.heal_go.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.heal_go.data.network.response.LoginResponse
import com.example.heal_go.data.network.response.UserData
import com.example.heal_go.data.network.response.UserEntity
import com.example.heal_go.data.network.response.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class OnboardingRepository(private val context: Context) {

    suspend fun saveToDataStore(isFinished: Boolean) {
        context.dataStore.edit { preference ->
            preference[KEY] = isFinished
            preference[USER_NAME] = ""
            preference[USER_EMAIL] = ""
            preference[TOKEN] = ""
            preference[LOGIN_DATE] = ""
            preference[STATE] = false
        }
    }

    suspend fun createLoginSession(session: UserSession) {
        context.dataStore.edit { preference ->
            preference[USER_NAME] = session.sessions.data?.user?.name ?: ""
            preference[USER_EMAIL] = session.sessions.data?.user?.email ?: ""
            preference[TOKEN] = session.sessions.data?.token ?: ""
            preference[LOGIN_DATE] = session.sessions.login_date
            preference[STATE] = session.sessions.state
        }
    }

    suspend fun clearLoginSession() {
        context.dataStore.edit { preference ->
            preference[USER_NAME] = ""
            preference[USER_EMAIL] = ""
            preference[TOKEN] = ""
            preference[LOGIN_DATE] = ""
            preference[STATE] = false
        }
    }

    val readFromDataStore: Flow<UserSession> = context.dataStore.data
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
            val name = preference[USER_NAME] ?: ""
            val email = preference[USER_EMAIL] ?: ""
            val token = preference[TOKEN] ?: ""
            val login_date = preference[LOGIN_DATE] ?: ""
            val state = preference[STATE] ?: false

            UserSession(
                isFinised,
                LoginResponse(
                    data = UserData(UserEntity(name, email), token = token),
                    login_date = login_date,
                    state = state
                )
            )
        }

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")

        private val KEY = booleanPreferencesKey("onboarding")
        private val USER_NAME = stringPreferencesKey("name")
        private val USER_EMAIL = stringPreferencesKey("email")
        private val TOKEN = stringPreferencesKey("token")
        private val LOGIN_DATE = stringPreferencesKey("login_date")
        private val STATE = booleanPreferencesKey("state")
    }
}