package com.example.heal_go.data.network.api

import com.example.heal_go.data.network.response.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /*code below will be updated after server ready*/
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @GET("home")
    suspend fun getAllDestinations(
        @Header("Authorization") auth: String
    ) : DestinationResponse
    
    @POST("recommendation")
    suspend fun recommendation(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ) : RecommendationResponse

    @FormUrlEncoded
    @POST("discover")
    suspend fun getDataDiscover(
        @Header("Authorization") token: String,
        @Field("page") page: Int?,
        @Field("destination") destination: String?,
        @Field("category") category: String?
    ) : DiscoverResponse
}