package com.example.heal_go.util

import com.example.heal_go.data.network.api.ApiConfig
import com.example.heal_go.data.repository.MainRepository

object Injection {
    fun provideMainRepository() : MainRepository {
        val apiService = ApiConfig().getApiService()

        return MainRepository(apiService)
    }
}