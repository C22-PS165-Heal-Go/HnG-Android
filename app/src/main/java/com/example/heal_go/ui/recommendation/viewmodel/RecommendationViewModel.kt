package com.example.heal_go.ui.recommendation.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.example.heal_go.R
import com.example.heal_go.data.network.response.SwipeResponse
import com.example.heal_go.data.repository.MainRepository
import com.example.heal_go.data.repository.RecommendationTutorialRepository
import com.example.heal_go.util.Status
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class RecommendationViewModel(
    private val mainRepository: MainRepository,
    private val tutorialRepository: RecommendationTutorialRepository,
    private val context: Context
) : ViewModel() {

    private val _swipeResponse = MutableLiveData<Status<SwipeResponse?>>()
    val swipeResponse: LiveData<Status<SwipeResponse?>> get() = _swipeResponse

    fun onTutorialFinish() {
        viewModelScope.launch {
            tutorialRepository.saveToDataStore(true)
        }
    }

    fun getTutorialDatastore(): LiveData<Boolean> {
        return tutorialRepository.readFromDataStore.asLiveData()
    }

    fun sendSwipeRecommendation(token: String, recommendations: ArrayList<JSONObject>) {
        _swipeResponse.value = Status.Loading(true)

        try {
            viewModelScope.launch {
                val response =
                    mainRepository.sendSwipeRecommendation("Bearer $token", recommendations)
                _swipeResponse.value = Status.Success(response)
            }
        } catch (e: Exception) {
            _swipeResponse.value = Status.Error(context.getString(R.string.server_error))
        } finally {
            _swipeResponse.value = Status.Loading(false)
        }
    }
}