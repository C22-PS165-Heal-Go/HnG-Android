package com.example.heal_go.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
