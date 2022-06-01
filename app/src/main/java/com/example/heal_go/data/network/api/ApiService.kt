package com.example.heal_go.data.network.api

import com.example.heal_go.data.network.response.LoginResponse
import com.example.heal_go.data.network.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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

    @GET("/destination")
    suspend fun getAllDestinations() : LoginResponse
}