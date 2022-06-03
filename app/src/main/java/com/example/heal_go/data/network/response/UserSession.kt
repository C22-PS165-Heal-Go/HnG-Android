package com.example.heal_go.data.network.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSession(
    var first_time_key: Boolean,
    var sessions: LoginResponse
) : Parcelable
