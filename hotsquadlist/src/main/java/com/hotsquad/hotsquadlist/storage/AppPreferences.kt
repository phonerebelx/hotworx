package com.hotsquad.hotsquadlist.storage

import com.hotsquad.hotsquadlist.model.response.LoginResponse
import com.hotsquad.hotsquadlist.utils.Constant.PreferenceKeys.LOGIN_RESPONSE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.commons.lang3.StringUtils

object AppPreferences
{
    var loginData: LoginResponse?
        get() {
            val data = Prefs.getString(LOGIN_RESPONSE, StringUtils.EMPTY)
            if (data.isNotEmpty()) {
                return Gson().fromJson(data, object : TypeToken<LoginResponse>() {}.type)
            }
            return null
        }
        set(loginData) = Prefs.putString(LOGIN_RESPONSE, Gson().toJson(loginData))
}