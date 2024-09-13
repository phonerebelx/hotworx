package com.passio.modulepassio.helpers

import android.content.Context
import android.provider.Settings

class UtilsHelpers {
    companion object {
        fun getDeviceId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }
    }
}