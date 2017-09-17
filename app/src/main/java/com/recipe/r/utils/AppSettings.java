package com.recipe.r.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
/**
 * 作者：Administrator on 2017/6/9 14:01
 * 功能:@描述轻量级存储
 * 缓存数据
 */
public class AppSettings {



    public static String getPrefString(Context context, String key,
                                       final String defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    public static Boolean getPrefString(Context context, String key,
                                        final Boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    public static int getPrefString(Context context, String key,
                                    final int defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }


    public static void setPrefString(Context context, final String key,
                                     final String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).commit();
    }

    public static void setPrefString(Context context, final String key,
                                     final Boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).commit();
    }


    public static void setPrefString(Context context, final String key,
                                     final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).commit();
    }


    public static void clearPreference(Context context,
                                       final SharedPreferences p) {
        final Editor editor = p.edit();

        editor.clear();
        editor.commit();
    }


    public static void removePreference(Context context, final String key) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().remove(key).commit();
    }

    public static int getAppVersionNumber(Context context) {
        int nRet = 100;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            nRet = info.versionCode;
        } catch (Exception e) {
            Log.e("AppSettings", "getAppVersionNumber() Error : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return nRet;
    }

    public static String getAppVersion(Context context) {
        String stRet = "1.0.0";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            stRet = info.versionName;
        } catch (Exception e) {
            Log.e("AppSettings", "getAppVersionNumber() Error : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return stRet;
    }

    public static String getAppPackegName(Context context) {
        String stRet = "qiloo.sz.mainfun";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            stRet = info.packageName;
        } catch (Exception e) {
            Log.e("AppSettings", "getAppPackegName() Error : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return stRet;
    }


}
