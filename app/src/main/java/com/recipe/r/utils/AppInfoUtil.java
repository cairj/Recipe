package com.recipe.r.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @功能：APP信息的工具类
 * @作者：wang
 */
public class AppInfoUtil {
  
    /**
     * 获取版本Code
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
        } catch (NameNotFoundException e) {
            Logger.exception(e);
        }
        int versionCode = 1;
        if (packInfo != null) {
            versionCode = packInfo.versionCode;
        }
        return versionCode;
    }

    /**
     * 获取版本Name
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        String versionName = "1.0.0";
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
        } catch (NameNotFoundException e) {
            Logger.exception(e);
        }
        if (packInfo != null) {
            versionName = packInfo.versionName;
        }
        return versionName;
    }

    /**
     * 获取app配置文件里面的meta-data键值对
     *
     * @param key 需要传入的关键字
     * @return 返回关键字所对应的值
     */
    public static String getMetaData(String key, Context context) {
        String result = "";
        try {
            result = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA).metaData
                    .getString(key);
        } catch (NameNotFoundException e) {
            Logger.exception(e);
        }
        return result;
    }

    /**
     * 获取app签名
     */
    public static String getSignInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];

            // 将sign转化为字节数
            byte[] signBytes = sign.toByteArray();

            // 解析sign字节数组
            try {
                CertificateFactory certFactory = CertificateFactory
                        .getInstance("X.509");
                X509Certificate cert = (X509Certificate) certFactory
                        .generateCertificate(new ByteArrayInputStream(signBytes));
                String signNumber = cert.getSerialNumber().toString();

                return signNumber;

            } catch (CertificateException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }

    /**
     * 安装apk
     */
    public static void installApk(File file, Context context) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
