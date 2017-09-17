package com.recipe.r.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by hht on 2016/12/14.
 * 检查请求权限
 */

public class CheckPermissionUtils {
    private Activity context;

    public CheckPermissionUtils(Activity context) {
        this.context = context;
    }

    //从本地相册选取照片
    public boolean checkExternalStorage(int requestCode) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    requestCode
            );
            return false;
        } else {
            return true;
        }
    }

    //调用相机拍照
    public boolean checkCameraPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    requestCode
            );
            return false;
        } else {
            return true;
        }
    }

    //获取联系人
    public boolean checkReadContactPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{
                            Manifest.permission.READ_CONTACTS
                    },
                    requestCode
            );
            return false;
        } else {
            return true;
        }
    }

    //获取LOCATION
    public boolean checkLOCATIONPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    requestCode
            );
            return false;
        } else {
            return true;
        }
    }

    //获取PHONESTATE
    public boolean checkPHONESTATEPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE
                    },
                    requestCode
            );
            return false;
        } else {
            return true;
        }
    }

    //
    public boolean checkSingleCameraPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    requestCode
            );
            return false;
        } else {
            return true;
        }
    }
}
