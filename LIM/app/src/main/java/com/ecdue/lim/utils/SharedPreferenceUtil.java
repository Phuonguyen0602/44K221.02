package com.ecdue.lim.utils;

import android.content.Context;

public class SharedPreferenceUtil {
    public static final String PERMISSION_PREF = "permission_pref";
    public static final String USER_PREF = "user_pref";

    public static final String PREF_KEY_ALLOW_NOTIFICATION = "allow_notification";
    public static final String PREF_KEY_HAS_REQUEST_STORAGE_BEFORE = "has_request_storage_before";
    public static final String PREF_KEY_HAS_REQUEST_CAMERA_BEFORE = "has_request_camera_before";

    public static boolean getHasRequestStorageBefore(Context context){
        return context.getSharedPreferences(PERMISSION_PREF, Context.MODE_PRIVATE).
                getBoolean(PREF_KEY_HAS_REQUEST_STORAGE_BEFORE, false);
    }
    public static void setHasRequestStorageBefore(Context context, boolean value){
        context.getSharedPreferences(PERMISSION_PREF, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(PREF_KEY_HAS_REQUEST_STORAGE_BEFORE, value)
                .apply();
    }
    public static boolean getHasRequestCameraBefore(Context context){
        return context.getSharedPreferences(PERMISSION_PREF, Context.MODE_PRIVATE).
                getBoolean(PREF_KEY_HAS_REQUEST_CAMERA_BEFORE, false);
    }
    public static void setHasRequestCameraBefore(Context context, boolean value){
        context.getSharedPreferences(PERMISSION_PREF, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(PREF_KEY_HAS_REQUEST_CAMERA_BEFORE, value)
                .apply();
    }
    public static boolean getAllowNotification(Context context){
        return context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
                .getBoolean(PREF_KEY_ALLOW_NOTIFICATION, true);
    }
    public static void setAllowNotification(Context context, boolean value){
        context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(PREF_KEY_ALLOW_NOTIFICATION, value)
                .apply();
    }
}
