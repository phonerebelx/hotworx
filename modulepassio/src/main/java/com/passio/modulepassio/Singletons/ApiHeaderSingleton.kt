package com.passio.modulepassio.Singletons

import android.content.Context
import com.passio.modulepassio.helpers.BasePreferenceHelper

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
}