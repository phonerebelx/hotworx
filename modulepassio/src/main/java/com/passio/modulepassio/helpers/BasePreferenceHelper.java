package com.passio.modulepassio.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;


public class BasePreferenceHelper extends PreferenceHelper {

    private Context context;
    private static final String FILENAME = "preferences";
    private static final String KEY_LOGIN_STATUS = "islogin";
    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_IS_DIET = "key_is_diet";
    private static final String TOKEN_KEY = "token_key";
    private static final String INTERMITTENT_DATA_KEY = "intermittent_data_key";
    private static final String INTERMITTENT_STATUS = "intermittent_status";
    private static final String SESSION_START_STATUS = "GET_SESSION_START";
    private static final String LOGIN_DATA_KEY = "loginDataKey";
    private static final String KEY_DEVICE_ID = "key_device_id";
    private static final String KEY_INTEGER = "KEY_INTEGER";
    private String KEY_ACTIVE_SESSION = "active_session";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_OTP_MAIN_TOKEN = "KEY_OTP_MAIN_TOKEN";
    private static final String KEY_SET_USER_DATA_MODEL = "KEY_SET_USER_DATA_MODEL";
    private static final String KEY_SET_USER_DATA_MODEL_FOR_FORGOT_PASSWORD = "KEY_SET_USER_DATA_MODEL_FOR_FORGOT_PASSWORD";

    private static final String KEY_NOTIFICATION_STRING = "KEY_NOTIFICATION_STRING";

    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public SharedPreferences getSharedPreferencess() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void setLoginStatus(boolean isLogin) {
        putBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS, isLogin);
    }

    public boolean isLogin() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }

    public String getUserId() {
        return getStringPreference(context, FILENAME, KEY_USER_ID);
    }

    public void putUserId(String user_id) {
        putStringPreference(context, FILENAME, KEY_USER_ID, user_id);
    }

    public void putLoginToken(String token) {
        setToken(context, KEY_TOKEN, FILENAME, token);
    }

    public String getLoginToken() {
        return getToken(context, FILENAME, KEY_TOKEN);
    }

    public void putOtpMainToken(String token) {
        setOtpMainToken(context, KEY_OTP_MAIN_TOKEN, FILENAME, token);
    }

    public String getOtpMainToken() {
        return getOtpMainToken(context, FILENAME, KEY_OTP_MAIN_TOKEN);
    }

}
