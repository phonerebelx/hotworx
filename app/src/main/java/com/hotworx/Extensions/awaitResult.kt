package com.hotworx.Extensions
import com.hotworx.activities.DockActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> Call<T>.await(context: DockActivity, showLoader: Boolean): T {
    if (showLoader) context.onLoadingStarted()

    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (showLoader) context.onLoadingFinished()
                if (response.isSuccessful) {
                    response.body()?.let { continuation.resume(it) }
                        ?: continuation.resumeWithException(NullPointerException("Response body is null"))
                } else {
                    continuation.resumeWithException(Exception(response.errorBody()?.string()))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (showLoader) context.onLoadingFinished()
                continuation.resumeWithException(t)
            }
        })

        continuation.invokeOnCancellation {
            try {
                cancel()
            } catch (ex: Exception) {

            }
        }
    }
}
