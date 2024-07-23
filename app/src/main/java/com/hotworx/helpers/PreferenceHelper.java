package com.hotworx.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hotworx.models.ForgotPassword.ForgotPasswordUserDetail;
import com.hotworx.models.UserDataModel;

/**
 * Class that can be extended to make available simple preference
 * setter/getters.
 * 
 * Should be extended to provide Facades.
 * 
 */
public class PreferenceHelper {

	protected void putStringPreference( Context context, String prefsName,	String key, String value ) {
		SharedPreferences preferences = context.getSharedPreferences( prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString( key, value );
		editor.commit();

	}

	protected String getStringPreference( Context context, String prefsName,
			String key ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		String value = preferences.getString( key, "");
		return value;
	}

	protected void putBooleanPreference( Context context, String prefsName, String key, boolean value ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();

		editor.putBoolean( key, value );
		editor.commit();
	}

	protected boolean getBooleanPreference( Context context, String prefsName,
			String key ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		boolean value = preferences.getBoolean( key, false );
		return value;
	}

	protected void putIntegerPreference( Context context, String prefsName,
			String key, int value ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();

		editor.putInt( key, value );
		editor.commit();
	}

	/**
	 * Get a integer under a particular key and filename
	 *
	 * @param context
	 * @param the
	 *            filename of preferences
	 * @param key
	 *            name of preference
	 * @return -1 if key is not found
	 */
	protected int getIntegerPreference( Context context, String prefsName,
			String key ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		int value = preferences.getInt( key, -1);
		return value;
	}

	protected void putLongPreference( Context context, String prefsName,
			String key, long value ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();

		editor.putLong( key, value );
		editor.commit();
	}

	/**
	 * Get a integer under a particular key and filename
	 *
	 * @param context
	 * @param the
	 *            filename of preferences
	 * @param key
	 *            name of preference
	 * @return Integer.MIN if key is not found
	 */
	protected long getLongPreference( Context context, String prefsName,
			String key ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		long value = preferences.getLong( key, Integer.MIN_VALUE );
		return value;
	}

	protected void putFloatPreference( Context context, String prefsName,
			String key, float value ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();
		editor.putFloat( key, value );
		editor.commit();
	}

	protected float getFloatPreference( Context context, String prefsName,
			String key ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		float value = preferences.getFloat( key, Float.MIN_VALUE );
		return value;
	}

	protected void removePreference( Context context, String prefsName, String key ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.remove( key );
		editor.commit();
	}

	protected boolean setToken(Context context,String key ,String prefsName ,String token) {
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, token);
		editor.apply();
		editor.commit();
		return true;
	}
	protected String getToken( Context context, String prefsName, String key ) {
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		return preferences.getString( key, null);
	}

	protected boolean setOtpMainToken(Context context,String key ,String prefsName ,String token) {
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, token);
		editor.apply();
		editor.commit();
		return true;
	}

	protected String getOtpMainToken( Context context, String prefsName, String key ) {
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		return preferences.getString( key, null);
	}

	protected Boolean setUserName(Context context,String key ,String prefsName ,String userName){
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, userName);
		editor.apply();
		editor.commit();
		return true;
	}


	protected String getUserName(Context context,String prefsName,String key ){
		SharedPreferences preferences = context.getSharedPreferences(prefsName, Activity.MODE_PRIVATE );
		return preferences.getString( key, null);
	}

	protected Boolean setImagePath(Context context,String key ,String prefsName ,String ImagePath){
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, ImagePath);
		editor.apply();
		editor.commit();
		return true;
	}
	protected String getImagePath(Context context,String prefsName,String key ){
		SharedPreferences preferences = context.getSharedPreferences(prefsName, Activity.MODE_PRIVATE );
		return preferences.getString( key, null);
	}

	protected boolean setUserDetail(Context context,String key ,String prefsName ,UserDataModel data ) {
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		Gson gson = new GsonBuilder().create();

		SharedPreferences.Editor editor = preferences.edit();
		UserDataModel userData = data;
		userData.setUserName(data.getUserName());
		userData.setPassword(data.getPassword());
		userData.setPhone_number(data.getPhone_number());
		userData.setType(data.getType());
		String jsonString = gson.toJson(data);
		editor.putString(key, jsonString);
		editor.apply();
		return true;
	}

	protected UserDataModel getUserDetail(Context context,String key ,String prefsName ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		String value = preferences.getString( key, null);
		if (value != null) {
			Gson gson = new GsonBuilder().create();
			UserDataModel userDataModel = gson.fromJson(value, UserDataModel.class);
			return userDataModel;
		}
		return new UserDataModel("","","","");
	}

	protected boolean setUserDetailForForgotPassword(Context context,String key ,String prefsName ,ForgotPasswordUserDetail data ) {
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		Gson gson = new GsonBuilder().create();
		SharedPreferences.Editor editor = preferences.edit();
		String jsonString = gson.toJson(data);
		editor.putString(key, jsonString);
		editor.apply();
		return true;
	}

	protected ForgotPasswordUserDetail getUserDetailForForgotPassword(Context context,String key ,String prefsName ) {

		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		String value = preferences.getString( key, null);
		if (value != null) {
			Gson gson = new GsonBuilder().create();
			ForgotPasswordUserDetail userDataModelForForgotPassword = gson.fromJson(value, ForgotPasswordUserDetail.class);
			return userDataModelForForgotPassword;
		}
		return new ForgotPasswordUserDetail("","","","");
	}

	protected Boolean setCheckFlow(Context context,String key,String prefsName,Integer data){
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, data);
		editor.apply();
		editor.commit();
		return true;
	}

	protected Integer getCheckFlow(Context context,String prefsName,String key ){
		SharedPreferences preferences = context.getSharedPreferences(prefsName, Activity.MODE_PRIVATE );
		return preferences.getInt( key, 0);
	}
}
