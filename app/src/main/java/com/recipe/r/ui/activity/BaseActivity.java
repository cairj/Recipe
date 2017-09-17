package com.recipe.r.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.recipe.r.R;
import com.recipe.r.base.BaseApplication;
import com.recipe.r.events.MessageEvent;
import com.recipe.r.ui.dialog.WaitingDialog;
import com.recipe.r.ui.widget.CustomDownEndView;
import com.recipe.r.ui.widget.CustomRefreshHeadView;
import com.recipe.r.utils.AppManager;
import com.recipe.r.utils.Logger;
import com.recipe.r.utils.ToastUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 所有activity基类
 * wangxiaoer
 * 功能描述：EventBus注册发送消息
 * 网络请求进度条显示方法
 **/
public class BaseActivity extends FragmentActivity {
    public WaitingDialog mWaitingDialog;
    private ImageView iv_return_left;
    private LinearLayout lv_head_return;
    private ImageView iv_return_right;
    private TextView tv_return_left;
    private TextView tv_return_right;
    private TextView tv_head_title;
    public MyOkHttp mMyOkhttp = null;
    public Context context = this;
    public String TAG = "" + this.getClass();
    private RelativeLayout head_rl;
    private TextView shopCartNum;
    private LinearLayout rl_head;

    @SuppressLint({"InlinedApi", "ResourceAsColor"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        // 设置状态栏
        setTranslucentStatus(R.color.main_color);
        mMyOkhttp = BaseApplication.getInstance().getMyOkHttp();
        Log.e("当前Activity:", "" + this);
        // 注册事件总线，不要在onStart和onResume中注册，会有问题：xx already registered to event class xxx
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 参数一：左边图标显示图标,需要隐藏时传0
     * 参数二：左边文字,需要隐藏时传null或空字符
     * 参数三：中间标题文字,需要隐藏时传null或空字符
     * 参数四五分别为右边
     *
     * @param
     */
    public void initHead(int leftimage, String leftstr, String titlestr, int rightimage, String rightstr) {
        head_rl = (RelativeLayout) findViewById(R.id.head_rl);
        lv_head_return = (LinearLayout) findViewById(R.id.lv_head_return);
        iv_return_left = (ImageView) findViewById(R.id.iv_return_left);
        rl_head = (LinearLayout) findViewById(R.id.rl_head);
        if (leftimage != 0) {
            iv_return_left.setVisibility(View.VISIBLE);
            iv_return_left.setBackgroundDrawable(getResources().getDrawable(leftimage));

        } else {
            iv_return_left.setVisibility(View.GONE);
        }
        tv_return_left = (TextView) findViewById(R.id.tv_return_left);
        if (!TextUtils.isEmpty(leftstr)) {
            tv_return_left.setVisibility(View.VISIBLE);
            tv_return_left.setText(leftstr);
        } else {
            tv_return_left.setVisibility(View.GONE);
        }
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        if (!TextUtils.isEmpty(titlestr)) {
            tv_head_title.setVisibility(View.VISIBLE);
            tv_head_title.setText(titlestr);
        } else {
            tv_head_title.setVisibility(View.GONE);
        }
        iv_return_right = (ImageView) findViewById(R.id.iv_return_right);
        if (rightimage != 0 || !TextUtils.isEmpty(rightstr)) {
            rl_head.setVisibility(View.VISIBLE);
        } else {
            rl_head.setVisibility(View.GONE);
        }
        if (rightimage != 0) {
            iv_return_right.setVisibility(View.VISIBLE);
            iv_return_right.setBackgroundDrawable(getResources().getDrawable(rightimage));
        } else {
            iv_return_right.setVisibility(View.GONE);
        }
        tv_return_right = (TextView) findViewById(R.id.tv_return_right);
        if (!TextUtils.isEmpty(rightstr)) {
            tv_return_right.setVisibility(View.VISIBLE);
            tv_return_right.setText(rightstr);
        } else {
            tv_return_right.setVisibility(View.GONE);
        }
        shopCartNum = (TextView) findViewById(R.id.shopCartNum);
    }

    public void initRefresh(SwipeToLoadLayout swipeToLoadLayout) {
        if (swipeToLoadLayout != null) {
            CustomRefreshHeadView swipe_refresh_header = (CustomRefreshHeadView) findViewById(R.id.swipe_refresh_header);
            CustomDownEndView swipe_load_more_footer = (CustomDownEndView) findViewById(R.id.swipe_load_more_footer);
            swipeToLoadLayout.setRefreshHeaderView(swipe_refresh_header);
            swipeToLoadLayout.setLoadMoreFooterView(swipe_load_more_footer);
        }
    }

    public void SetHeadBackground(int color_str) {
        if (head_rl != null) {
            head_rl.setBackgroundColor(color_str);
        }
    }

    public TextView getTitleView() {
        return this.tv_head_title;
    }

    public LinearLayout getReturnView() {
        return lv_head_return;
    }

    public ImageView getLeftView() {
        return this.iv_return_left;
    }

    public ImageView getRightView() {
        return this.iv_return_right;
    }

    public TextView getLeftTextView() {
        return tv_return_left;
    }

    public TextView getRightTextView() {
        return tv_return_right;
    }

    public TextView getShopView() {
        return shopCartNum;
    }

    /**
     * 接收到EventBus发布的消息事件
     *
     * @param event 消息事件
     */
    @CallSuper
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(@NonNull MessageEvent event) {
        Logger.e("", "onReceiveMessage..." + (event == null ? "null" : event));
    }

    /**
     * 发送消息，用于各个组件之间通信，所有的消息事件只能通过MessageEvent发送
     *
     * @param event 消息事件对象
     */
    protected final void sendMessage(@NonNull MessageEvent event) {
        // 发布EventBus消息事件
        EventBus.getDefault().post(event);
    }

    /**
     * 设置状态栏背景状态
     */
    @SuppressLint("InlinedApi")
    public void setTranslucentStatus(int colorTitler) {
        // 判断是否是4.4及以上版本、
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setNavigationBarTintResource(colorTitler);
        tintManager.setStatusBarTintResource(colorTitler);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyOkhttp != null) {
            mMyOkhttp.cancel(this);
        }
        AppManager.getInstance().killActivity(this);
        // 取消注册事件总线，不要在onStop中取消注册，当Activity被别的页面遮住了就会有问题
        if (unregisterEventBusOnDestroy()) {
            EventBus.getDefault().unregister(this);
        }
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.showDialog(false);
        }
    }

    /**
     * 是否要Activity销毁时取消注册EventBus
     *
     * @return 返回true表示要取消，否则不取消，默认为不取消
     */
    public boolean unregisterEventBusOnDestroy() {
        return false;
    }

    /**
     * 显示Toast提示信息
     *
     * @param message 消息文本
     */
    public final void showToast(@NonNull String message) {
        //默认显示0.5秒
        ToastUtil.show(this, message, 500);
    }

    /**
     * 显示Toast提示信息
     *
     * @param resId string.xml中的字符串资源ID
     */
    public final void showToast(@StringRes int resId) {
        //默认显示0.5秒
        ToastUtil.show(this, this.getResources().getString(resId), 500);
    }

    /**
     * 显示请求网络progressBar
     */
    public void showProgress() {
        try {
            if (null == mWaitingDialog) {
                mWaitingDialog = new WaitingDialog(this);
                mWaitingDialog.setWaitText("加载中...");
            }
            mWaitingDialog.showDialog(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏请求网络progressBar
     */
    public void hideProgress() {
        try {
            if (mWaitingDialog != null && mWaitingDialog.isShowing()) {

                mWaitingDialog.showDialog(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\:*?<>|\"\n\t]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 系统内存监控
     */
    public void displayBriefMemory() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        Logger.i("displayBriefMemory", "系统剩余内存:" + (info.availMem >> 10) + "k");
        Logger.i("displayBriefMemory", "当系统剩余内存低于" + info.threshold
                + "时就看成低内存运行");
    }

    /**
     * 检测EditText不能为空;
     *
     * @param ets
     * @return true：表示EditTexts都不为空； false:表示有EditTexts为空；
     */
    public boolean chkEditText(EditText... ets) {
        for (EditText et : ets) {
            String str = et.getText().toString();
            if (TextUtils.isEmpty(str)) {
                // et.setError(et.getHint() + "不能为空");
                return false;
            }
        }
        return true;
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyBoard() {
        if (this.getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(this.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 通过dispatchTouchEvent每次ACTION_DOWN事件中动态判断非EditText本身区域的点击事件， 然后在事件中进行屏蔽。
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 隐藏掉edittext的弹窗
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

//    /**
//     * 系统返回按钮
//     */
//    private int count = 0;
//    private long time = 0;
//
//    @Override
//    public void onBackPressed() {
//        AppManager am = AppManager.getInstance();
//        // 如果activity栈内activity个数>1
//        if (am.getTopActivity().equals(this) && am.getActivities().size() > 1) {
//            super.onBackPressed();
//            return;
//        }
//        // 如果activity栈内activity个数=1;
//        if (count == 0) {
//            Toast.makeText(this, R.string.exit_again, Toast.LENGTH_SHORT).show();
//            count++;
//            time = System.currentTimeMillis();
//            return;
//        } else {
//            long nowTime = System.currentTimeMillis();
//            if (nowTime - time <= 2000) {
//                Logger.e("BaseActivity", "退出");
//                // AsyncHttpRequestUtil.dispose(this);
//                AppManager.getInstance().exit(this);
//            } else {
//                count = 0;
//            }
//        }
//    }

    // dp转换为px像素值
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
