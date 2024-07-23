package com.hotworx.helpers;

import android.content.Context;

public class ApplicationManager extends PreferenceHelper {
    private static ApplicationManager instance;
    private static Context ctx;
    private static final String FILENAME = "my_preferences";
    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final String KEY_PARENT_ACTIVITY_ID = "KEY_PARENT_ACTIVITY_ID";
    private static final String KEY_LIST_SIZE = "KEY_LIST_SIZE";
    private static final String KEY_SESSION_ID = "KEY_SESSION_ID";

//    private String activityId = "0";
//    private String parentActivityId = "0";
//    private int listSize = 0;
//    private int sessionId = 0;

    public static ApplicationManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ApplicationManager.class) {
                if (instance == null) {
                    instance = new ApplicationManager(context);
                }
            }
        }
        //Return instance
        return instance;
    }

    private ApplicationManager(Context context) {
        ctx = context;
    }

    public String getParentActivityId() {
        return getStringPreference(ctx, FILENAME, KEY_PARENT_ACTIVITY_ID).equals("")?"0":getStringPreference(ctx, FILENAME, KEY_PARENT_ACTIVITY_ID);
    }

    public void setParentActivityId(String parentActivityId) {
        putStringPreference(ctx, FILENAME, KEY_PARENT_ACTIVITY_ID, parentActivityId);
    }

    public String getActivityId() {
        return getStringPreference(ctx, FILENAME, KEY_ACTIVITY_ID).equals("")?"0":getStringPreference(ctx, FILENAME, KEY_ACTIVITY_ID);
    }

    public void setActivityId(String activityId) {
        putStringPreference(ctx, FILENAME, KEY_ACTIVITY_ID, activityId);
    }

    public int getListSize() {
        return getIntegerPreference(ctx, FILENAME, KEY_LIST_SIZE) == -1 ? 0 : getIntegerPreference(ctx, FILENAME, KEY_LIST_SIZE);
    }

    public void setListSize(int listSize) {
        putIntegerPreference(ctx, FILENAME, KEY_LIST_SIZE, listSize);
    }


    public void setSessionId(int sessionId) {
        putIntegerPreference(ctx, FILENAME, KEY_SESSION_ID, sessionId);
    }

    public int getSessionId() {
        return getIntegerPreference(ctx, FILENAME, KEY_SESSION_ID)==-1? 0 : getIntegerPreference(ctx, FILENAME, KEY_SESSION_ID);
    }
}
