package com.passio.modulepassio.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class OSHelper {
	
	public static boolean isExerternalStorageAvailable() {
		return Environment.getExternalStorageState().contentEquals(
				Environment.MEDIA_MOUNTED ) ? true : false;
	}
	
	public static boolean isInternetAvailable( Context context ) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = conn.getActiveNetworkInfo();
		if (activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
