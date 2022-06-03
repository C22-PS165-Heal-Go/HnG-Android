package com.example.heal_go.data.repository

import android.util.ArrayMap
import android.util.Log
import com.example.heal_go.data.network.api.ApiService
import com.example.heal_go.data.network.response.LoginResponse
import com.example.heal_go.data.network.response.RecommendationResponse
import com.example.heal_go.data.network.response.RegisterResponse
import com.example.heal_go.util.timeStamp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response


/*there will be some updates after server ready*/
class MainRepository(private val apiService: ApiService) {

    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val response = apiService.login(email, password)
            response
        } catch (e: Exception) {
            LoginResponse(code = e.hashCode(), login_date = timeStamp, state = false)
        }
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return try {
            val response = apiService.register(name, email, password)
            response
        } catch (e: Exception) {
            RegisterResponse(code = e.hashCode())
        }
    }

    suspend fun getRecommendation(
        token: String,
        questionOne: Int,
        questionTwo: List<Int>,
        questionThree: List<Int>,
        questionFour: Int,
        questionFive: Int,
        questionSix: Int,
        questionSeven: Int,
        questionEight: Int
    ): RecommendationResponse {

        val jsonObject = JSONObject()
        jsonObject.put("member", questionOne)
        jsonObject.put("age", JSONArray(questionTwo))
        jsonObject.put("activity", JSONArray(questionThree))
        jsonObject.put("sport", questionFour)
        jsonObject.put("day", questionFive)
        jsonObject.put("time", questionSix)
        jsonObject.put("gender", questionSeven)
        jsonObject.put("price", questionEight)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        return try {
           val response = apiService.recommendation(
                "Bearer $token",
                requestBody
            )
            response
        } catch (e: Exception) {
            RecommendationResponse(code = e.hashCode())
        }

    }


    /*fun getAllDestinations() : LiveData<LoginResponse> = liveData {
        try {
            val response = apiService.getAllDestinations()
            emit(response)
        } catch (e: Exception) {
            emit(LoginResponse())
        }
    }*/
}