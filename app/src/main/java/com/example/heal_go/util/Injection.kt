package com.example.heal_go.util

import android.content.Context
import com.example.heal_go.data.network.api.ApiConfig
import com.example.heal_go.data.repository.MainRepository
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.data.repository.RecommendationTutorialRepository
import com.example.heal_go.ui.recommendation.RecommendationCardActivity

object Injection {
    fun provideMainRepository() : MainRepository {
        val apiService = ApiConfig().getApiService()

        return MainRepository(apiService)
    }

    fun provideRecommendationTutorialRepository(context: Context) : RecommendationTutorialRepository =
        RecommendationTutorialRepository(context)

    /*fun provideOnBoardingRepository(context: Context) = OnboardingRepository(context)*/
}