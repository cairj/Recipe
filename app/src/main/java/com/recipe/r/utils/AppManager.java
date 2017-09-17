package com.recipe.r.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;


/**
 * 控制各个Activityy的活动
 * Antivity的管理类,控制Activity的退出和关闭
 *
 * @author wangxiaoer
 *         <p/>
 */
public class AppManager {
    public Stack<Activity> mActivityStack;//用来保存全部的Activity
    private static AppManager mAppManager;
    private final static String LOG_TAG = "AppManager";

    private AppManager() {
    }

    public Stack<Activity> getActivities() {
        return this.mActivityStack;
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (mAppManager == null) {
            mAppManager = new AppManager();
        }
        return mAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
        Logger.i(LOG_TAG, "addActivity(" + activity.getClass().getSimpleName()
                + ")");
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void killTopActivity() {
        Activity activity = mActivityStack.lastElement();
        killActivity(activity);

    }

    /**
     * 结束指定的Activity
     */
    public void killActivity(Activity activity) {
        if (activity != null) {
            Logger.i(LOG_TAG, "addActivity("
                    + activity.getClass().getSimpleName() + ")");
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        for (int i = 0; i < mActivityStack.size(); i++) {
            Activity activity = mActivityStack.get(i);
            if (activity.getClass().equals(cls)) {
                killActivity(activity);
            }
        }
    }

    /**
     * 结束指定类名的其他Activity
     */
    public void killOtherActivity(Class<?> cls) {
        for (int i = 0; i < mActivityStack.size(); i++) {
            Activity activity = mActivityStack.get(i);
            if (!(activity.getClass().equals(cls))) {
                killActivity(activity);
            }
        }
        if (mActivityStack.size() != 1) {
            killOtherActivity(cls);
        }
    }

    /**
     * 结束所有Activity
     */
    public void killAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void exit(Context context) {
        Logger.e("AppManager", "退出应用程序");
        try {
            killAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            android.os.Process.killProcess(android.os.Process.myPid());
            System.gc();
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public boolean IsExit() {
        if (mActivityStack.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}