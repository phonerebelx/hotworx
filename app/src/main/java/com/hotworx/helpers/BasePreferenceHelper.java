package com.hotworx.helpers;

import static com.passio.modulepassio.data.PassioDemoSharedPreferences.PREF_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.hotworx.models.ActiveSessionModel;
import com.hotworx.models.ForgotPassword.ForgotPasswordUserDetail;
import com.hotworx.models.UserData.DataX;
import com.hotworx.models.UserDataModel;
import com.hotworx.requestEntity.IntermittentPlanResponse;
import com.hotworx.requestEntity.ViewProfileResponse;
import com.hotworx.retrofit.GsonFactory;
import com.google.gson.GsonBuilder;


public class BasePreferenceHelper extends PreferenceHelper {

    private Context context;
    private static final String FILENAME = "preferences";
    private static final String KEY_LOGIN_STATUS = "islogin";
    private static final String KEY_USER_ID= "key_user_id";
    private static final String KEY_IS_DIET= "key_is_diet";
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
        putStringPreference(context, FILENAME, KEY_USER_ID,user_id);
    }

    public String getIsDietTrax() {
        return getStringPreference(context, FILENAME, KEY_IS_DIET);
    }

    public void putIsDietTrax(String is_diet) {
        putStringPreference(context, FILENAME, KEY_IS_DIET,is_diet);
    }

    public void putLoginToken(String token){
        setToken(context,KEY_TOKEN,FILENAME,token);
    }

    public String getLoginToken(){
        return getToken(context,FILENAME,KEY_TOKEN);
    }

    public void putOtpMainToken(String token){
        setOtpMainToken(context,KEY_OTP_MAIN_TOKEN,FILENAME,token);
    }

    public String getOtpMainToken(){
        return getOtpMainToken(context,FILENAME,KEY_OTP_MAIN_TOKEN);
    }

    public void setUserDataModel(UserDataModel data){
        setUserDetail(context,KEY_SET_USER_DATA_MODEL,FILENAME,data);
    }

    public UserDataModel getUserDataModel(){
        return getUserDetail(context,KEY_SET_USER_DATA_MODEL,FILENAME);
    }

    public void setUserName(String userName){
        setUserName(context,"USERNAME_GENERATOR",FILENAME,userName);
    }

    public String getUserName(){
        return getUserName(context,FILENAME,"USERNAME_GENERATOR");
    }

    public void setImagePath(String ImagePath){
        setImagePath(context,"IMAGE_GENERATOR",FILENAME,ImagePath);
    }

    public String  getImagePath(){
        return getImagePath(context,FILENAME,"IMAGE_GENERATOR");
    }


    public void setUserDataForForgotPassword(ForgotPasswordUserDetail data){
        setUserDetailForForgotPassword(context,KEY_SET_USER_DATA_MODEL_FOR_FORGOT_PASSWORD,FILENAME,data);
    }
    public ForgotPasswordUserDetail getUserDetailForForgotPassword(){
        return getUserDetailForForgotPassword(context,KEY_SET_USER_DATA_MODEL_FOR_FORGOT_PASSWORD,FILENAME);
    }
    public ViewProfileResponse getLoginData() {
        String obj = getStringPreference(context, FILENAME, LOGIN_DATA_KEY);
        return GsonFactory.getConfiguredGson().fromJson(obj, ViewProfileResponse.class);
    }

    //it is now save after otp
    public void putLoginData(DataX user) {
        putStringPreference(context,FILENAME,LOGIN_DATA_KEY,
                new GsonBuilder().create().toJson(user));
        if (user == null) {
            clearPreferences();
        }
    }

    private void clearPreferences() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Remove the specific preferences
        editor.remove("foodRecords");
        editor.remove("weightRecords");
        editor.remove("waterRecords");
        editor.remove("foodFavorites");
        editor.remove("userProfile");

        // Apply the changes
        editor.apply();
    }

    public void saveString(String key, String value) {
        putStringPreference(context,FILENAME,key, value);
    }

    public String getString(String key) {
        return getStringPreference(context,FILENAME,key);
    }

    public void setActiveSession(ActiveSessionModel sessionModel) {
        putStringPreference(context,FILENAME,KEY_ACTIVE_SESSION,
                new GsonBuilder().create().toJson(sessionModel));
    }

    public ActiveSessionModel getActiveSession() {
        String json = getStringPreference(context, FILENAME,KEY_ACTIVE_SESSION);
        if (json != null) {
            return new GsonBuilder().create().fromJson(json, ActiveSessionModel.class);
        }
        return null;
    }

    public void removeActiveSession() {
        removePreference(context,FILENAME,KEY_ACTIVE_SESSION);
    }

    public IntermittentPlanResponse.Setting_data.Plan_data getIntermittentData() {
        String obj = getStringPreference(context, FILENAME, INTERMITTENT_DATA_KEY);
        return GsonFactory.getConfiguredGson().fromJson(obj, IntermittentPlanResponse.Setting_data.Plan_data.class);
    }

    public void putIntermittentData(IntermittentPlanResponse.Setting_data.Plan_data user) {
        putStringPreference(context,FILENAME,INTERMITTENT_DATA_KEY, new GsonBuilder().create().toJson(user));
    }

    public Boolean getIntermittentStatus() {
        return getBooleanPreference(context, FILENAME, INTERMITTENT_STATUS);
    }

    public void putIntermittentStatus(Boolean status) {
        putBooleanPreference(context, FILENAME, INTERMITTENT_STATUS, status);
    }


    public Boolean getSessionStart() {
        return getBooleanPreference(context, FILENAME, SESSION_START_STATUS);
    }

    public void setSessionStart(Boolean status) {
        putBooleanPreference(context, FILENAME, SESSION_START_STATUS, status);
    }


    public void setDeviceId(String deviceId) {
        putStringPreference(context, FILENAME, KEY_DEVICE_ID, deviceId);
    }

    public String getDeviceId() {
        return getStringPreference(context, FILENAME, KEY_DEVICE_ID);
    }

    public String getNotificationString() {
        return getStringPreference(context, FILENAME, KEY_NOTIFICATION_STRING);
    }

    public void setNotificationString(String notificationString) {
        putStringPreference(context, FILENAME, KEY_NOTIFICATION_STRING, notificationString);
    }

    public void setCheckFlow(Integer data){
        setCheckFlow(context,KEY_INTEGER,FILENAME,data);
    }

    public Integer getCheckFlow() {
        return getCheckFlow(context, FILENAME, KEY_INTEGER);
    }
}
