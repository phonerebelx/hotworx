package com.hotsquad.hotsquadlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotsquad.hotsquadlist.koin.InjectUtils
import com.hotsquad.hotsquadlist.model.request.LoginRequest
import com.hotsquad.hotsquadlist.model.response.LoginResponse
import com.hotsquad.hotsquadlist.network.ApiResponseCallback
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = InjectUtils.appRepository

    /**
     * Baking Property For Login Request
     * Get Response in [loginResponse] Live Data
     */
    private val _loginResponse = MutableLiveData<ApiResponseCallback<LoginResponse>>()
    val loginResponse: LiveData<ApiResponseCallback<LoginResponse>>
        get() = _loginResponse
    fun loginRequest(loginRequest: LoginRequest) {
        _loginResponse.value = ApiResponseCallback.Loading()
        viewModelScope.launch {
            _loginResponse.value = repository.userRepository.login(loginRequest)
        }
    }
}