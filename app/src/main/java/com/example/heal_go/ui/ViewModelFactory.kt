package com.example.heal_go.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heal_go.ui.auth.viewmodel.AuthViewModel
import com.example.heal_go.ui.dashboard.viewmodel.DashboardViewModel
import com.example.heal_go.ui.questionnaire.viewmodel.QuestionnaireViewModel
import com.example.heal_go.ui.recommendation.viewmodel.RecommendationViewModel
import com.example.heal_go.util.Injection

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(Injection.provideMainRepository(), context) as T
            }
            modelClass.isAssignableFrom(QuestionnaireViewModel::class.java) -> {
                QuestionnaireViewModel(Injection.provideMainRepository(), context) as T
            }
            modelClass.isAssignableFrom(RecommendationViewModel::class.java) -> {
                RecommendationViewModel(Injection.provideMainRepository(), Injection.provideRecommendationTutorialRepository(context), context) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(Injection.provideMainRepository(), context) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown viewmodel class")
            }
        }
    }
}