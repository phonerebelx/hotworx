package com.hotworx.Singletons

import android.content.Context
import com.hotworx.helpers.BasePreferenceHelper

object ApiHeaderSingleton {
    @JvmStatic
    fun  apiHeader(context: Context): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        val prefHelper = BasePreferenceHelper(context)
        headerMap.apply {
            put("Authorization", "Bearer " + prefHelper.loginToken)
        }
        return headerMap
    }

    fun apiHeaderBrivo(context: Context, getTokenValue: String): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        val prefHelper = BasePreferenceHelper(context)
        headerMap.apply {
            put("Host", "api.brivo.com")
            put("Authorization", "Bearer $getTokenValue")
            put("api-key", "m5eauus6j7xdhe88g9kvbx9x")
        }
        return headerMap
    }

}