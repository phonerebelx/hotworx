package com.hotsquad.hotsquadlist.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import com.hotsquad.hotsquadlist.R
import com.hotsquad.hotsquadlist.koin.InjectUtils

object AppToast
{
    private var mToast: Toast? = null

    fun showToast(toastMessage: String) {
        createToast(toastMessage, Toast.LENGTH_SHORT)
    }

    fun showToast(@StringRes resId: Int) {
        createToast(InjectUtils.appContext.getString(resId), Toast.LENGTH_SHORT)
    }

    fun showLongToast(toastMessage: String) {
        createToast(toastMessage, Toast.LENGTH_LONG)
    }

    fun showLongToast(@StringRes resId: Int) {
        createToast(InjectUtils.appContext.getString(resId), Toast.LENGTH_LONG)
    }

    fun showInternetErrorToast() {
        createToast(InjectUtils.appContext.getString(R.string.error_msg_no_internet), Toast.LENGTH_LONG)
    }

    private fun createToast(string: String?, toastDuration: Int) {
        mToast?.cancel()
        mToast = Toast.makeText(InjectUtils.appContext, string, toastDuration)
        Handler(Looper.getMainLooper()).post {
            try {
                mToast?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}