package com.hotsquad.hotsquadlist.network.domain

import com.hotsquad.hotsquadlist.network.ApiResponseCallback


suspend fun <T> performNetworkCallOperation(
    networkCall: suspend () -> ApiResponseCallback<T>,
): ApiResponseCallback<T> {

    val responseStatus = networkCall.invoke()
    responseStatus.data?.let { response ->
        return ApiResponseCallback.Success(response)
    }
    return ApiResponseCallback.Error(responseStatus.message, responseStatus.code)
}

suspend fun <T> performNetworkCallOperation(
    networkCall: suspend () -> ApiResponseCallback<T>,
    saveCallResult: suspend (T) -> Unit
): ApiResponseCallback<T> {

    val responseStatus = networkCall.invoke()
    responseStatus.data?.let { response ->
        saveCallResult(response)
        return ApiResponseCallback.Success(response)
    }
    return ApiResponseCallback.Error(responseStatus.message, responseStatus.code)
}