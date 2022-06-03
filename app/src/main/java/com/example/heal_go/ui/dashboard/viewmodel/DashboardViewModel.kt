package com.example.heal_go.ui.dashboard.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heal_go.data.network.response.DestinationResponse
import com.example.heal_go.data.repository.MainRepository
import com.example.heal_go.util.Status
import kotlinx.coroutines.launch
import java.lang.Exception

class DashboardViewModel(private val mainRepository: MainRepository, context: Context) :
    ViewModel() {
    private val _destinations = MutableLiveData<Status<DestinationResponse?>>()
    val destinations: LiveData<Status<DestinationResponse?>> get() = _destinations

    fun getAllDestinations(token: String) {
        _destinations.value = Status.Loading(true)

        try {
            viewModelScope.launch {
                val response = mainRepository.getAllDestinations("Bearer $token")
                _destinations.value = Status.Success(response)
            }
        } catch (e: Exception) {
            _destinations.value = Status.Error("Server Error")
        } finally {
            _destinations.value = Status.Loading(false)
        }
    }
}