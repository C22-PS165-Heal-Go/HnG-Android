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
data class DestinationResponse(

    @field:SerializedName("data")
    val data: ArrayList<DestinationItem>? = null,

    @field:SerializedName("success")
    val success: Boolean = false,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("code")
    val code: Int? = null
) : Parcelable

@Parcelize
data class DestinationItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("location")
    val location: String? = null
) : Parcelable

@Parcelize
data class DateDiff(
    val seconds: Long,
    val minutes: Long,
    val hours: Long,
    val days: Long
) : Parcelable