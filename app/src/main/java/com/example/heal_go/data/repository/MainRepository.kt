package com.example.heal_go.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.heal_go.data.network.api.ApiService
import com.example.heal_go.data.network.response.LoginResponse
import java.lang.Exception

/*there will be some updates after server ready*/
class MainRepository(private val apiService: ApiService) {

    fun login(email: String, password: String) : LiveData<LoginResponse> = liveData {
        try {
            val response = apiService.login(email, password)
            emit(response)
        } catch (e: Exception) {
            emit(LoginResponse())
        }
    }

    fun register(name: String, email: String, password: String) : LiveData<LoginResponse> = liveData {
        try {
            val response = apiService.register(name, email, password)
            emit(response)
        } catch (e: Exception) {
            emit(LoginResponse())
        }
    }

    fun getAllDestinations() : LiveData<LoginResponse> = liveData {
        try {
            val response = apiService.getAllDestinations()
            emit(response)
        } catch (e: Exception) {
            emit(LoginResponse())
        }
    }
}