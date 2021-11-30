package com.example.android_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Classe pour factoriser les test de permission
 */

public abstract class CheckPermission {

    public static final int READ_WRITE_STORAGE_PERMISSION_REQUEST_CODE = 1;
    public static final int INTERNET_PERMISSION_REQUEST_CODE = 2;
    public static final int READ_WRITE_INTERNET_PERMISSION_REQUEST_CODE = 3;

    public static boolean checkPermissionForReadExternalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public static boolean checkPermissionForInternet(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.INTERNET);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

}
