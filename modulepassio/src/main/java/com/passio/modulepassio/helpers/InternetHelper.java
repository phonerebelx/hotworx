package com.passio.modulepassio.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class InternetHelper {

    public static boolean CheckInternetConectivity(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
            return true;
        } else {
            return false;
        }
    }


}
