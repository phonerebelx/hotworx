package com.hotsquad.hotsquadlist.repository

import com.hotsquad.hotsquadlist.koin.InjectUtils
import com.hotsquad.hotsquadlist.model.request.LoginRequest
import com.hotsquad.hotsquadlist.network.domain.performNetworkCallOperation
import com.hotsquad.hotsquadlist.storage.AppPreferences

class UserRepository {

    private var remoteDataSource = InjectUtils.dataSource

    suspend fun login(loginRequest: LoginRequest) = performNetworkCallOperation(
        networkCall = {
            remoteDataSource.login(loginRequest)
        },
        saveCallResult = {
            if (it.success!!) {
                AppPreferences.loginData = it
            }
        }
    )

}