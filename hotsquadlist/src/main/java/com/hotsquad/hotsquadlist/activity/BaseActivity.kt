package com.hotsquad.hotsquadlist.activity

import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hotsquad.hotsquadlist.network.domain.APIError
import com.hotsquad.hotsquadlist.network.domain.APIError.Companion.INTERNAL_SERVER_ERROR
import com.hotsquad.hotsquadlist.network.domain.APIError.Companion.NETWORK_CALL_FAILED
import com.hotsquad.hotsquadlist.network.domain.APIError.Companion.PAGE_NOT_FOUND
import com.hotsquad.hotsquadlist.network.domain.APIError.Companion.SERVER_BAD_REQUEST
import com.hotsquad.hotsquadlist.network.domain.APIError.Companion.UNAUTHORIZED
import com.hotsquad.hotsquadlist.network.domain.APIError.Companion.UNEXPECTED_ERROR_OCCURRED
import com.hotsquad.hotsquadlist.network.domain.APIError.Companion.WEB_SERVER_ERROR
import com.hotsquad.hotsquadlist.network.domain.APIError.Companion.WEB_SERVICE_UNAVAILABLE
import com.hotsquad.hotsquadlist.network.domain.ErrorHandler
import com.hotsquad.hotsquadlist.storage.AppPreferences
import com.hotsquad.hotsquadlist.databinding.LayoutLoadingBinding
import com.hotsquad.hotsquadlist.extensions.gotoActivityWithNoHistory
import com.hotsquad.hotsquadlist.extensions.showMaterialAlertDialog
import com.hotsquad.hotsquadlist.extensions.visible
import com.hotsquad.hotsquadlist.listener.DialogListeners
import com.hotsquad.hotsquadlist.network.ApiResponseCallback

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Set the application Orientation
         */
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun showProgressIndicator(layoutLoadingBinding: LayoutLoadingBinding) {
        layoutLoadingBinding.root.visible(true)
    }

    fun hideProgressIndicator(layoutLoadingBinding: LayoutLoadingBinding) {
        layoutLoadingBinding.root.visible(false)
    }

    fun <T> genericNetworkErrorHandler(
        response: ApiResponseCallback<T>,
        passError: (ErrorHandler) -> Unit
    ) {
        APIError.networkCallFailed(response) { errorHandler ->

            /**
             * Pass the current error if you want to handle different error status
             */
            passError(errorHandler)

            when (errorHandler.errorStatus) {
                UNAUTHORIZED -> {
                    showMaterialAlertDialog(getString(errorHandler.messageID), object :
                        DialogListeners {
                        override fun onPositionButtonTap(dialog: DialogInterface?) {
                            dialog?.dismiss()
                            AppPreferences.loginData = null
                            gotoActivityWithNoHistory(LoginActivity::class.java)
                        }
                    })
                }
                APIError.BLOCK_BY_ADMIN_MSG -> {
                    showMaterialAlertDialog(getString(errorHandler.messageID), object :
                        DialogListeners {
                        override fun onPositionButtonTap(dialog: DialogInterface?) {
                            dialog?.dismiss()
                            AppPreferences.loginData = null
                            gotoActivityWithNoHistory(LoginActivity::class.java)
                        }
                    })
                }
                NETWORK_CALL_FAILED,
                PAGE_NOT_FOUND,
                SERVER_BAD_REQUEST,
                WEB_SERVICE_UNAVAILABLE,
                WEB_SERVER_ERROR,
                INTERNAL_SERVER_ERROR,
                UNEXPECTED_ERROR_OCCURRED -> {
                    showMaterialAlertDialog(getString(errorHandler.messageID))
                }
                else -> {
                    showMaterialAlertDialog(errorHandler.message)
                }
            }
        }
    }
}