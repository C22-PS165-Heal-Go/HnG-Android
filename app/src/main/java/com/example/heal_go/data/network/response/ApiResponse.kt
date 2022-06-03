package com.example.heal_go.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @field:SerializedName("code")
    var code : Int? = null,

    @field:SerializedName("success")
    var success : Boolean? = false,

    @field:SerializedName("message")
    var message : String? = null,

    @field:SerializedName("data")
    var data: UserData? = null,

    var login_date: String,

    var state: Boolean
) : Parcelable

@Parcelize
data class UserData (
    @field:SerializedName("user")
    var user: UserEntity,

    @field:SerializedName("token")
    var token: String
) : Parcelable

@Parcelize
data class UserEntity (
    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("email")
    var email: String
) : Parcelable

@Parcelize
data class RegisterResponse(
    @field:SerializedName("code")
    var code : Int? = null,

    @field:SerializedName("success")
    var success : Boolean? = false,

    @field:SerializedName("message")
    var message : String? = null
) : Parcelable

@Parcelize
data class RecommendationResponse(
    @field:SerializedName("code")
    var code : Int? = null,

    @field:SerializedName("data")
    val data: List<RecommendationDataItem?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
): Parcelable

@Parcelize
data class RecommendationDataItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("id")
    val id: String? = null
): Parcelable

@Parcelize
data class DateDiff(
    val seconds: Long,
    val minutes: Long,
    val hours: Long,
    val days: Long
) : Parcelable

@Parcelize
data class DestinationDetail(
    val id: String,
    val imageUrl: String,
    val name: String,
    val location: String,
    val description: String
): Parcelable
