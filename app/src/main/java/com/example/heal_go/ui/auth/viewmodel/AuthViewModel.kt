package com.example.heal_go.ui.auth.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heal_go.R
import com.example.heal_go.data.network.response.LoginResponse
import com.example.heal_go.data.network.response.RegisterResponse
import com.example.heal_go.data.repository.MainRepository
import com.example.heal_go.util.Status
import com.example.heal_go.util.timeStamp
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel(private val repository: MainRepository, private val context: Context) : ViewModel() {

    private val _login = MutableLiveData<Status<LoginResponse?>>()
    val login: LiveData<Status<LoginResponse?>> get() = _login

    private val _register = MutableLiveData<Status<RegisterResponse?>>()
    val register: LiveData<Status<RegisterResponse?>> get() = _register

    fun userLoginHandler(email: String, password: String) {
        _login.value = Status.Loading(true)

        try {
            viewModelScope.launch {
                val callback = repository.login(email, password)
                callback.login_date = timeStamp
                callback.state = true

                _login.value = Status.Success(callback)
            }
        } catch (e: Exception) {
            _login.value = Status.Error(context.getString(R.string.server_error))
        } finally {
            _login.value = Status.Loading(false)
        }
    }

    fun register(name: String, email: String, password: String) {
        _register.value = Status.Loading(true)

        try {
            /*throw data to repository below*/
            viewModelScope.launch {
                val callback = repository.register(name, email, password)
                _register.value = Status.Success(callback)
            }
        } catch (e: Exception) {
            _register.value = Status.Error(context.getString(R.string.server_error))
        } finally {
            _register.value = Status.Loading(false)
        }
    }
}