package com.example.heal_go.ui.auth.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heal_go.data.network.response.LoginResponse
import com.example.heal_go.data.repository.MainRepository
import com.example.heal_go.util.Status
import java.lang.Exception

class AuthViewModel(private val repository: MainRepository, context: Context) : ViewModel() {

    private val _login = MutableLiveData<Status<LoginResponse?>>()
    val login : LiveData<Status<LoginResponse?>> get() = _login

    private val _register = MutableLiveData<Status<LoginResponse?>>()
    val register : LiveData<Status<LoginResponse?>> get() = _register

    fun userLoginHandler(email: String, password: String) {
        _login.value = Status.Loading(true)

        try {
            /*throw data to repository below*/
            /*val callback = repository.login(email, password)
            _login.value = Status.Success(callback.value)*/
        } catch (e: Exception) {
            _login.value = Status.Error("Server Error")
        } finally {
            _login.value = Status.Loading(false)
        }
    }

    fun register(name: String, email: String, password: String) {
        _register.value = Status.Loading(true)

        try {
            /*throw data to repository below*/

        } catch (e: Exception) {
            _register.value = Status.Error("Server Error")
        } finally {
            _register.value = Status.Loading(false)
        }
    }
}