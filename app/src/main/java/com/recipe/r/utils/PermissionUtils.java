package com.recipe.r.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 2017/8/7
 * wangxiaoer
 * 功能描述：拍照权限检测
 **/
public class PermissionUtils {
    private static String[] PERMISSIONS_CAMERA_AND_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public static boolean isCameraPermission(Activity context, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            int storagePermission = ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if (storagePermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, PERMISSIONS_CAMERA_AND_STORAGE,
                        requestCode);
                return false;
            }
        }
        return true;
    }

    public static boolean checkCameraPermission(Activity context, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, requestCode);
                return false;
            }
        }
        return true;
    }

}
