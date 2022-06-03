package com.example.heal_go.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.heal_go.data.network.api.ApiService
import com.example.heal_go.data.network.response.DestinationResponse
import com.example.heal_go.data.network.response.LoginResponse
import com.example.heal_go.data.network.response.RegisterResponse
import com.example.heal_go.util.timeStamp
import java.lang.Exception

/*there will be some updates after server ready*/
class MainRepository(private val apiService: ApiService) {

    suspend fun login(email: String, password: String) : LoginResponse {
        return try {
            val response = apiService.login(email, password)
            response
        } catch (e: Exception) {
            LoginResponse(code = e.hashCode(), login_date = timeStamp, state = false)
        }
    }

    suspend fun register(name: String, email: String, password: String) : RegisterResponse {
        return try {
            val response = apiService.register(name, email, password)
            response
        } catch (e: Exception) {
            RegisterResponse(code = e.hashCode())
        }
    }

    suspend fun getAllDestinations(auth: String) : DestinationResponse {
        return try {
            val response = apiService.getAllDestinations(auth)
            response
        } catch (e: Exception) {
            DestinationResponse(code = e.hashCode())
        }
    }
}