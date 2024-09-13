package com.passio.modulepassio;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;


public class BaseApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();


        BaseApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return BaseApplication.context;
    }

    //!! THIS SHOULD BE IN AN ANDROID KEYSTORE!! See https://developer.android.com/training/articles/keystore.html
    private static final String CLIENT_SECRET = "43108e529fccd8ff1379cabf6b622565";

    /**
     * This key was generated using the SecureKeyGenerator [java] class. Run as a Java application (not Android)
     * This key is used to encrypt the authentication token in Android user preferences. If someone decompiles
     * your application they'll have access to this key, and access to your user's authentication token
     */
    //!! THIS SHOULD BE IN AN ANDROID KEYSTORE!! See https://developer.android.com/training/articles/keystore.html
    private static final String SECURE_KEY = "CVPdQNAT6fBI4rrPLEn9x0+UV84DoqLFiNHpKOPLRW0=";
}
