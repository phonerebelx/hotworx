package com.hotsquad.hotsquadlist.network.domain

import com.hotsquad.hotsquadlist.network.ApiResponseCallback
import com.hotsquad.hotsquadlist.utils.Utils
import com.hotsquad.hotsquadlist.R
import retrofit2.Response

data class ErrorHandler(
    val message: String = "",
    val messageID: Int = 0,
    val errorStatus: String
)

class APIError {

    companion object {

        private const val UNEXPECTED_ERROR = 11001
        private const val NO_NETWORK = 11002
        private const val UNAUTHENTICATED = 401
        private const val NOT_FOUND = 404
        private const val BLOCK_BY_ADMIN = 403
        private const val BAD_REQUEST = 400
        private const val SERVICE_UNAVAILABLE = 503
        private const val INTERNAL_SERVER = 500
        private const val SERVER_ERROR = 600

        const val UNAUTHORIZED = "unauthorized"
        const val NETWORK_CALL_FAILED = "network_failed"
        const val PAGE_NOT_FOUND = "not_found"
        const val BLOCK_BY_ADMIN_MSG = "Account_block"
        const val SERVER_BAD_REQUEST = "bad_request"
        const val WEB_SERVICE_UNAVAILABLE = "service_unavailable"
        const val WEB_SERVER_ERROR = "server_error"
        const val INTERNAL_SERVER_ERROR = "internal_server_error"
        const val UNEXPECTED_ERROR_OCCURRED = "unexpected_error"

        fun <T> error(error: Throwable): ApiResponseCallback<T> {
            return ApiResponseCallback.Error(error.toString(), UNEXPECTED_ERROR)
        }

        fun <T> error(response: Response<T>): ApiResponseCallback<T> {

            val code = response.code()
            val message = response.message()

            return ApiResponseCallback.Error(
                "Network call has failed for a following reason: $message",
                code
            )
        }

        fun <T> networkCallFailed(
            apiResponseCallback: ApiResponseCallback<T>,
            errorHandler: (ErrorHandler) -> Unit
        ) {
            emitError(apiResponseCallback, errorHandler)
        }

        private fun <T> emitError(
            apiResponseCallback: ApiResponseCallback<T>,
            errorHandler: (ErrorHandler) -> Unit
        ) {

            /**
             * Check For Internet Connection
             */
            if (!Utils.isOnline()) {
                apiResponseCallback.code = NO_NETWORK
            }

            when (apiResponseCallback.code) {
                UNAUTHENTICATED -> {
                    errorHandler(
                        ErrorHandler(
                            messageID = R.string.error_msg_unauthorized,
                            errorStatus = UNAUTHORIZED
                        )
                    )
                }
                NO_NETWORK -> {
                    errorHandler(
                        ErrorHandler(
                            messageID = R.string.error_msg_no_internet,
                            errorStatus = NETWORK_CALL_FAILED
                        )
                    )
                }
                NOT_FOUND -> {
                    errorHandler(
                        ErrorHandler(
                            messageID = R.string.page_not_found,
                            errorStatus = PAGE_NOT_FOUND
                        )
                    )
                }
                BAD_REQUEST -> {
                    errorHandler(
                        ErrorHandler(
                            messageID = R.string.bad_request,
                            errorStatus = SERVER_BAD_REQUEST
                        )
                    )
                }
                SERVICE_UNAVAILABLE -> {
                    errorHandler(
                        ErrorHandler(
                            messageID = R.string.service_unavailable,
                            errorStatus = WEB_SERVICE_UNAVAILABLE
                        )
                    )
                }
                SERVER_ERROR -> {
                    errorHandler(
                        ErrorHandler(
                            messageID = R.string.server_error,
                            errorStatus = WEB_SERVER_ERROR
                        )
                    )
                }
                INTERNAL_SERVER -> {
                    errorHandler(
                        ErrorHandler(
                            messageID = R.string.internal_server_error,
                            errorStatus = INTERNAL_SERVER_ERROR
                        )
                    )
                }
                BLOCK_BY_ADMIN -> {
                    errorHandler(
                        ErrorHandler(
                            messageID = R.string.account_block_by_admin_msg,
                            errorStatus = BLOCK_BY_ADMIN_MSG
                        )
                    )
                }
                else -> {
                    errorHandler(
                        ErrorHandler(
                            messageID = R.string.unexpected_error_occurred,
                            errorStatus = UNEXPECTED_ERROR_OCCURRED
                        )
                    )
                }
            }
        }
    }
}