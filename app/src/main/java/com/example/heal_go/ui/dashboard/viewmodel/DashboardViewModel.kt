package com.example.heal_go.ui.dashboard.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.heal_go.R
import com.example.heal_go.data.network.response.DestinationResponse
import com.example.heal_go.data.network.response.DiscoverItem
import com.example.heal_go.data.repository.MainRepository
import com.example.heal_go.util.Status
import kotlinx.coroutines.launch
import java.lang.Exception

class DashboardViewModel(private val mainRepository: MainRepository, private val context: Context) :
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
            _destinations.value = Status.Error(context.getString(R.string.server_error))
        } finally {
            _destinations.value = Status.Loading(false)
        }
    }

    fun getDataDiscover(
        token: String,
        destination: String?,
        category: String?
    ): LiveData<PagingData<DiscoverItem>> =
        mainRepository.getDataDiscover("Bearer $token", destination, category).cachedIn(viewModelScope)
}