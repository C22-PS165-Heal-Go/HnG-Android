package com.example.heal_go.ui.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.heal_go.data.repository.OnboardingRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(private val onboardingRepository: OnboardingRepository) : ViewModel() {

    fun onBoardingFinish() {
        viewModelScope.launch {
            onboardingRepository.saveToDataStore(true)
        }
    }

    fun getOnboardingDatastore(): LiveData<Boolean> {
        return onboardingRepository.readFromDataStore.asLiveData()
    }
}