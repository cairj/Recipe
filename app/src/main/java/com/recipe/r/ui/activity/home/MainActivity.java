package com.recipe.r.ui.activity.home;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.ConfigApp;
import com.recipe.r.events.MainEvent;
import com.recipe.r.ui.activity.BaseActivity;
import com.recipe.r.ui.fragment.home.HomeFragmentMain;
import com.recipe.r.ui.fragment.menu.MenuFragmentMain;
import com.recipe.r.ui.fragment.mine.MineFragmentMain;
import com.recipe.r.ui.fragment.reserve.ReserveFragmentMain;
import com.recipe.r.ui.fragment.share.ShareFragmentMain;
import com.recipe.r.utils.AppSettings;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    //首页，菜单，订桌，分享，我的
    private LinearLayout home_ll, menu_ll, reserve_ll, share_ll, mine_ll;
    private ImageView home_iv, menu_tv_iv, reserve_iv, share_iv, mine_iv;
    private TextView home_tv, menu_tv_tv, reserve_tv, share_tv, mine_tv;
    private Fragment curFragment, HomeFragment, MenuFragment, ReserveFragment, ShareFragment, MineFragment;
    private String TYPE = "";
    private LinearLayout main_bottom;
    private ImageView line_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (TextUtils.isEmpty(ConfigApp.USERNAME)) {
            CrashReport.setUserId(AppSettings.getPrefString(this, ConfigApp.USERNAME, ""));//设置debugly用户ID用于统计
        }
        if (null != getIntent().getExtras()) {
            TYPE = getIntent().getExtras().getString("type");
        }
        initView();
        initListener();
//        LoginHyp(ConfigApp.HXUSERNAME + AppSettings.getPrefString(MainActivity.this, ConfigApp.USERID, ""));
    }

    /**
     * 初始化控件
     */
    private void initView() {
        main_bottom = (LinearLayout) findViewById(R.id.main_bottom);
        line_main = (ImageView) findViewById(R.id.line_main);
        home_ll = (LinearLayout) findViewById(R.id.home_ll);
        menu_ll = (LinearLayout) findViewById(R.id.menu_ll);
        reserve_ll = (LinearLayout) findViewById(R.id.reserve_ll);
        share_ll = (LinearLayout) findViewById(R.id.share_ll);
        mine_ll = (LinearLayout) findViewById(R.id.mine_ll);
        home_iv = (ImageView) findViewById(R.id.home_iv);
        menu_tv_iv = (ImageView) findViewById(R.id.menu_iv);
        reserve_iv = (ImageView) findViewById(R.id.reserve_iv);
        share_iv = (ImageView) findViewById(R.id.share_iv);
        mine_iv = (ImageView) findViewById(R.id.mine_iv);
        home_tv = (TextView) findViewById(R.id.home_tv);
        menu_tv_tv = (TextView) findViewById(R.id.menu_tv);
        reserve_tv = (TextView) findViewById(R.id.reserve_tv);
        share_tv = (TextView) findViewById(R.id.share_tv);
        mine_tv = (TextView) findViewById(R.id.mine_tv);
        if (HomeFragment == null) {
            HomeFragment = new HomeFragmentMain(this);
        }
        if (MenuFragment == null) {
            MenuFragment = new MenuFragmentMain(this);
        }
        if (ReserveFragment == null) {

            ReserveFragment = new ReserveFragmentMain(this);
        }
        if (ShareFragment == null) {
            ShareFragment = new ShareFragmentMain(this);
        }
        if (MineFragment == null) {
            MineFragment = new MineFragmentMain(this);
        }
        curFragment = MenuFragment;
        if (TextUtils.isEmpty(TYPE)) {
            //正常加载
            setHomeShow();
        } else if (TYPE.equals("main")) {
            //从订桌成功进入主页
            setHomeShow();
        } else if (TYPE.equals("menu")) {
            //从订桌成功进入点菜
            setMenuShow();
        }
    }

    /**
     * 初始化导航栏点击事件
     */
    private void initListener() {
        home_ll.setOnClickListener(this);
        menu_ll.setOnClickListener(this);
        reserve_ll.setOnClickListener(this);
        share_ll.setOnClickListener(this);
        mine_ll.setOnClickListener(this);
    }

    /**
     * is hide bootom
     *
     * @param isHide
     */
    public void hideMainBottom(boolean isHide) {
        if (isHide) {
            main_bottom.setVisibility(View.GONE);
            line_main.setVisibility(View.GONE);
        } else {
            main_bottom.setVisibility(View.VISIBLE);
            line_main.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param fragment
     */
    public void changeFragment(Fragment fragment) {
        if (curFragment != fragment) {
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();
            if (!fragment.isAdded()) {
                transaction.add(R.id.fl_fragment, fragment)
                        .hide(curFragment).show(fragment).commit();
            } else {
                transaction.hide(curFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
            }
            curFragment = fragment;
        } else {
            //如果是菜单界面,特殊处理
            if (TYPE.equals("menu")) {
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction();
                if (!fragment.isAdded()) {
                    transaction.add(R.id.fl_fragment, fragment)
                            .show(fragment).commit();
                } else {
                    transaction.show(fragment).commit(); // 隐藏当前的fragment，显示下一个
                }
                curFragment = fragment;
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_ll:
                setHomeShow();
                break;
            case R.id.menu_ll:
                setMenuShow();
                break;
            case R.id.reserve_ll:
                setTableShow();
                break;
            case R.id.share_ll:
                setShareShow();
                break;
            case R.id.mine_ll:
                hideMainBottom(false);
                setTranslucentStatus(R.color.main_red);
                home_iv.setBackgroundResource(R.mipmap.home_off);
                home_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                menu_tv_iv.setBackgroundResource(R.mipmap.menu_off);
                menu_tv_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                reserve_iv.setBackgroundResource(R.mipmap.reserve_off);
                reserve_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                share_iv.setBackgroundResource(R.mipmap.share_off);
                share_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
                mine_iv.setBackgroundResource(R.mipmap.mine_on);
                mine_tv.setTextColor(ContextCompat.getColor(this, R.color.main_red));
                changeFragment(MineFragment);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

//    /**
//     * 环信登录方法
//     */
//    private void LoginHyp(String userName) {
//        EMClient.getInstance().login(userName, ConfigApp.HXPWD, new EMCallBack() {//回调
//                    @Override
//                    public void onSuccess() {
//                        EMClient.getInstance().chatManager().loadAllConversations();
//                        EMClient.getInstance().groupManager().loadAllGroups();
//                        Log.d("main", "登录聊天服务器成功！");
//                    }
//
//                    @Override
//                    public void onProgress(int progress, String status) {
//
//                    }
//
//                    @Override
//                    public void onError(int code, String message) {
//                        Log.d("main", "登录聊天服务器失败！");
//                        switch (code) {
//                            // 网络异常 2
//                            case EMError.NETWORK_ERROR:
//                                Logger.e("", "网络错误 code: " + code + ", message:" + message);
//                                break;
//                            // 无效的用户名 101
//                            case EMError.INVALID_USER_NAME:
//                                Logger.e("", "无效的用户名 code: " + code + ", message:" + message);
//                                break;
//                            // 无效的密码 102
//                            case EMError.INVALID_PASSWORD:
//                                Logger.e("", "无效的密码 code: " + code + ", message:" + message);
//                                break;
//                            // 用户认证失败，用户名或密码错误 202
//                            case EMError.USER_AUTHENTICATION_FAILED:
//                                Logger.e("", "用户认证失败，用户名或密码错误 code: " + code + ", message:" + message);
//                                break;
//                            // 用户不存在 204
//                            case EMError.USER_NOT_FOUND:
//                                Logger.e("", "用户不存在 code: " + code + ", message:" + message);
//                                break;
//                            // 无法访问到服务器 300
//                            case EMError.SERVER_NOT_REACHABLE:
//                                Logger.e("", "无法访问到服务器 code: " + code + ", message:" + message);
//                                break;
//                            // 等待服务器响应超时 301
//                            case EMError.SERVER_TIMEOUT:
//                                Logger.e("", "等待服务器响应超时 code: " + code + ", message:" + message);
//
//                                break;
//                            // 服务器繁忙 302
//                            case EMError.SERVER_BUSY:
//                                Logger.e("", "服务器繁忙 code: " + code + ", message:" + message);
//                                break;
//                            // 未知 Server 异常 303 一般断网会出现这个错误
//                            case EMError.SERVER_UNKNOWN_ERROR:
//                                Logger.e("", "未知的服务器异常 code: " + code + ", message:" + message);
//                                break;
//                            default:
//                                Logger.e("", "ml_sign_in_failed code: " + code + ", message:" + message);
//                                break;
//                        }
//                    }
//                }
//        );
//    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执
    public void onEventMainThread(MainEvent events) {
        if (events.getMessage().equals("menu")) {
            setMenuShow();
        } else if (events.getMessage().equals("table")) {
            setTableShow();
        } else if (events.getMessage().equals("share")) {
            setShareShow();
        }
    }

    private void setHomeShow() {
        hideMainBottom(false);
        setTranslucentStatus(R.color.main_color);
        changeFragment(HomeFragment);
        home_iv.setBackgroundResource(R.mipmap.home_on);
        home_tv.setTextColor(ContextCompat.getColor(this, R.color.main_red));
        menu_tv_iv.setBackgroundResource(R.mipmap.menu_off);
        menu_tv_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        reserve_iv.setBackgroundResource(R.mipmap.reserve_off);
        reserve_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        share_iv.setBackgroundResource(R.mipmap.share_off);
        share_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        mine_iv.setBackgroundResource(R.mipmap.mine_off);
        mine_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
    }

    private void setMenuShow() {
        hideMainBottom(false);
        setTranslucentStatus(R.color.main_color);
        home_iv.setBackgroundResource(R.mipmap.home_off);
        home_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        menu_tv_iv.setBackgroundResource(R.mipmap.menu_on);
        menu_tv_tv.setTextColor(ContextCompat.getColor(this, R.color.main_red));
        reserve_iv.setBackgroundResource(R.mipmap.reserve_off);
        reserve_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        share_iv.setBackgroundResource(R.mipmap.share_off);
        share_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        mine_iv.setBackgroundResource(R.mipmap.mine_off);
        mine_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        changeFragment(MenuFragment);
    }

    private void setTableShow() {
        hideMainBottom(false);
        setTranslucentStatus(R.color.main_color);
        home_iv.setBackgroundResource(R.mipmap.home_off);
        home_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        menu_tv_iv.setBackgroundResource(R.mipmap.menu_off);
        menu_tv_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        reserve_iv.setBackgroundResource(R.mipmap.reserve_on);
        reserve_tv.setTextColor(ContextCompat.getColor(this, R.color.main_red));
        share_iv.setBackgroundResource(R.mipmap.share_off);
        share_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        mine_iv.setBackgroundResource(R.mipmap.mine_off);
        mine_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        changeFragment(ReserveFragment);
    }

    private void setShareShow() {
        hideMainBottom(false);
        setTranslucentStatus(R.color.main_color);
        home_iv.setBackgroundResource(R.mipmap.home_off);
        home_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        menu_tv_iv.setBackgroundResource(R.mipmap.menu_off);
        menu_tv_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        reserve_iv.setBackgroundResource(R.mipmap.reserve_off);
        reserve_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        share_iv.setBackgroundResource(R.mipmap.share_on);
        share_tv.setTextColor(ContextCompat.getColor(this, R.color.main_red));
        mine_iv.setBackgroundResource(R.mipmap.mine_off);
        mine_tv.setTextColor(ContextCompat.getColor(this, R.color.lightest_black));
        changeFragment(ShareFragment);
    }
}
