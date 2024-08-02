package com.hotsquad.hotsquadlist.network.domain

import com.hotsquad.hotsquadlist.koin.InjectUtils
import com.hotsquad.hotsquadlist.model.request.LoginRequest

class DataSource : BaseDataSource() {

    private var apiService = InjectUtils.getRetrofit

    /**
     * Define All webServices instances here &
     * Get results in [callApi] callback
     */
    suspend fun login(loginRequest: LoginRequest) = callApi {
        apiService.login(loginRequest)
    }
}