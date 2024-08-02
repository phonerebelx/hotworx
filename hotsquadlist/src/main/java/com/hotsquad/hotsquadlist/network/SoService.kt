package com.hotsquad.hotsquadlist.network

import com.hotsquad.hotsquadlist.model.request.LoginRequest
import com.hotsquad.hotsquadlist.model.request.RegistrationRequest
import com.hotsquad.hotsquadlist.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SoService
{
    @POST(Routes.LOGIN)
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST(Routes.REGISTRATION)
    suspend fun registration(
        @Body registrationRequest: RegistrationRequest
    ): LoginResponse
}