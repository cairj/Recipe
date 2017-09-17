package com.recipe.r.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.recipe.r.R;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.ui.activity.login.LoginActivity;
import com.recipe.r.ui.activity.login.NewLoginActivity;
import com.recipe.r.ui.dialog.AlertDialog;

/**
 * 2017
 * 07
 * 2017/7/5
 * wangxiaoer
 * 功能描述：用户是否登录
 **/
public class UserIsLogin {
    /**
     * 判断是否需要跳转登录界面
     *
     * @param context
     */
    public static boolean IsLogn(final Context context) {
        if (!"0".equals(AppSettings.getPrefString(context, ConfigApp.ISLOGIN, "")))

        {
            //未登录
            AlertDialog dialog = new AlertDialog(context);
            dialog.builder();
            dialog.setTitle("提示");
            dialog.setMsg("未登录账号,请登录后操作");
            dialog.setPositiveButton(context.getResources().getString(R.string.sure_str), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(AppSettings.getPrefString(context, ConfigApp.USERNAME, ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, NewLoginActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
            dialog.setNegativeButton(context.getResources().getString(R.string.cancel), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    return;
                }
            });
            dialog.show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否显示未登录信息
     *
     * @param context
     */
    public static boolean isShowLogin(final Context context) {
        if (!"0".equals(AppSettings.getPrefString(context, ConfigApp.ISLOGIN, ""))) {
            return true;
        } else {
            return false;
        }
    }
}
