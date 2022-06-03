package com.example.heal_go.ui.recommendation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.heal_go.data.repository.MainRepository
import com.example.heal_go.data.repository.RecommendationTutorialRepository
import kotlinx.coroutines.launch

class RecommendationViewModel(
    provideMainRepository: MainRepository,
    recommendationTutorialRepository: RecommendationTutorialRepository,
    context: Context
) : ViewModel() {

    private val tutorialRepository = recommendationTutorialRepository

    fun onTutorialFinish() {
        viewModelScope.launch {
            tutorialRepository.saveToDataStore(true)
        }
    }

    fun getTutorialDatastore(): LiveData<Boolean> {
        return tutorialRepository.readFromDataStore.asLiveData()
    }

    fun sendSwipeRecommendation(recommendations: ArrayList<Boolean>) {

    }
}