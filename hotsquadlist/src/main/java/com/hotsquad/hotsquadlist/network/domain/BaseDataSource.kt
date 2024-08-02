package com.hotsquad.hotsquadlist.network.domain

import com.hotsquad.hotsquadlist.network.ApiResponseCallback
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> callApi(call: suspend () -> Response<T>): ApiResponseCallback<T> {
        var exception = Exception("Network call has failed!")
        runCatching {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let {
                    return ApiResponseCallback.Success(it)
                }
            }
            return APIError.error(response)
        }.onFailure { e ->
            exception = e as Exception
        }
        return APIError.error(exception)
    }
}