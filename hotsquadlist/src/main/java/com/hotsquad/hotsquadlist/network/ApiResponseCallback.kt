package com.hotsquad.hotsquadlist.network

sealed class ApiResponseCallback<T>(
    val data: T? = null,
    var code: Int? = null,
    val message: String? = null
) {
    class Loading<T> : ApiResponseCallback<T>()
    class Success<T>(data: T) : ApiResponseCallback<T>(data)
    class Error<T>(message: String?, code: Int?, data: T? = null) : ApiResponseCallback<T>(data, code, message)
}