package com.recipe.r.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.tencent.bugly.crashreport.CrashReport;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * 作者：wangxiaoer on 2017/6/8 18:11
 * 功能:@描述
 * 关于GallyFinal:http://www.tuicool.com/articles/UNVJn2
 */
public class BaseApplication extends MultiDexApplication {
    private static Context mContext;
    private static BaseApplication mApplication;
    private MyOkHttp mMyOkHttp;
    //    private FunctionConfig functionConfig;
    EMOptions options = new EMOptions();

    @Override
    public void onCreate() {
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        mContext = this;
        mApplication = this;
        super.onCreate();
        SetOkHttpUtils();
//        setHYPKF();
        setBugly();
    }


    /**
     * 初始化http工具类
     */
    private void SetOkHttpUtils() {
        //持久化存储cookie
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mApplication));
        //自定义OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)       //设置开启cookie
                .build();
        mMyOkHttp = new MyOkHttp(okHttpClient);


    }



    private void setHYPKF() {
        ChatClient.Options options = new ChatClient.Options();
        options.setAppkey("1188170622115805#gussin");//必填项，appkey获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“AppKey”
        options.setTenantId("44403");//必填项，tenantId获取地址：kefu.easemob.com，“管理员模式 > 设置 > 企业信息”页面的“租户ID”
        // Kefu SDK 初始化
        if (!ChatClient.getInstance().init(this, options)) {
            return;
        }
        // Kefu EaseUI的初始化
        UIProvider.getInstance().init(this);
        //后面可以设置其他属性
    }

    //TODO 初始化Bugly
    private void setBugly() {
        // 获取当前包名
        String packageName = getApplicationContext().getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mApplication);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), Config.Bugly_ID, true, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


    public static Context getAppContext() {
        return mContext;
    }

    public synchronized static BaseApplication getInstance() {
        return mApplication;
    }

    public MyOkHttp getMyOkHttp() {
        return mMyOkHttp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
