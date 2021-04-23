package com.ecdue.lim.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class PermissionUtil {
    public static final String[] STORAGE_PERM = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static boolean requestStorageBefore = false;
    public static final String[] CAMERA_PERM = new String[]{Manifest.permission.CAMERA};
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static boolean requestCameraBefore = false;
    public static final int STORAGE_REQUEST = 0;
    public static final int CAMERA_REQUEST = 1;

    public static boolean hasPermission(Context context, String permission){
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean hasPermissions(Context context, String[] permissions){
        for (String per : permissions){
            if (!hasPermission(context, per))
                return false;
        }
        return true;
    }
}
