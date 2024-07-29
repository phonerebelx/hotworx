package com.hotworx.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.hotworx.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Timer;

import spencerstudios.com.bungeelib.Bungee;

public class SplashActivity extends BaseActivity {
    final int TIME_INTERVAL_TO_CHECK = 500;// in millis
    final int MIN_TIME_INTERVAL_FOR_SPLASH = 2500; // in millis
    boolean workComplete = false;
    Timer checkWorkTimer;
    Runnable backgroundWork = new Runnable() {
        @Override
        public void run() {
            workComplete = true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            if (!extras.containsKey("navigateTo")){
            intent.putExtra("navigateTo", "NotificationFragment");

            for (String key : extras.keySet()) {
                Object value = extras.get(key);

                if (value instanceof String || value instanceof Integer) {
                    if ( key.equals("id")){
                        intent.putExtra("hashId", value.toString());
                    }
                    if (key.equals("image") || key.equals("title") || key.equals("body")) {
                        intent.putExtra(key, value.toString());
                    }
                }
            }
        }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        launchTimerAndTask();
    }

    private void launchTimerAndTask() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainActivity();
            }
        }, MIN_TIME_INTERVAL_FOR_SPLASH);
    }

    private void showMainActivity() {

        if(prefHelper.getLoginToken() != null && !prefHelper.getLoginToken().isEmpty()){
            Intent intent = new Intent(this,MainActivity.class);
            if(getIntent()!=null){

                intent.putExtra("navigateTo",getIntent().getStringExtra("navigateTo"));
                intent.putExtra("hashId",getIntent().getStringExtra("hashId"));
                intent.putExtra("image",getIntent().getStringExtra("image"));
                intent.putExtra("body",getIntent().getStringExtra("body"));
                intent.putExtra("notification_type",getIntent().getStringExtra("notification_type"));
                intent.putExtra("custom_message",getIntent().getStringExtra("custom_message"));
                intent.putExtra("booking_date",getIntent().getStringExtra("booking_date"));
                intent.putExtra("title",getIntent().getStringExtra("title"));
                intent.putExtra("objid",getIntent().getStringExtra("objid"));
                intent.putExtra("calendar_title",getIntent().getStringExtra("calendar_title"));
                intent.putExtra("duration",getIntent().getIntExtra("duration",0));
                intent.putExtra("flag","");
            }

            intent.putExtra("flag","");
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            if(getIntent()!=null){
                intent.putExtra("navigateTo",getIntent().getStringExtra("navigateTo"));
                intent.putExtra("hashId",getIntent().getStringExtra("hashId"));
            }
            startActivity(intent);
        }
        finish();
        Bungee.split(SplashActivity.this);
    }

    public String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return key;
    }
}
