package com.example.heal_go.util

sealed class Status<out T> private constructor() {
    data class Success<out T>(val data: T) : Status<T>()
    data class Error<out T>(val error: String) : Status<T>()
    data class Loading<out T>(val status: Boolean) : Status<T>()
}