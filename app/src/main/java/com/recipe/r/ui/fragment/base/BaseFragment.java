package com.recipe.r.ui.fragment.base;/**
 * 作者：Administrator on 2017/6/9 14:12
 * 功能:@描述
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.recipe.r.R;
import com.recipe.r.ui.dialog.WaitingDialog;
import com.recipe.r.ui.widget.CustomDownEndView;
import com.recipe.r.ui.widget.CustomRefreshHeadView;
import com.tsy.sdk.myokhttp.MyOkHttp;

/**
 * 2017
 * 06
 * 2017/6/9
 * wangxiaoer
 * 功能描述：fragment基类
 **/
public class BaseFragment extends Fragment {

    private WaitingDialog mWaittingDialog;
    private ImageView iv_return_left;
    private ImageView iv_return_right;
    private TextView tv_return_left;
    private TextView tv_return_right;
    private RelativeLayout head_rl;
    private TextView tv_head_title;
    private LinearLayout lv_head_return;
    public MyOkHttp mMyOkhttp = null;
    private TextView shopCartNum;
    private LinearLayout rl_head;
    private LinearLayout head_right_ll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgress() {
        if (mWaittingDialog == null) {
            mWaittingDialog = new WaitingDialog(getActivity());
        }
        if (!mWaittingDialog.isShowing())
            mWaittingDialog.show();
    }

    public void hideProgress() {
        if (mWaittingDialog != null && mWaittingDialog.isShowing())
            mWaittingDialog.dismiss();
    }

    /**
     * 参数一：左边图标显示图标,需要隐藏时传0
     * 参数二：左边文字,需要隐藏时传null或空字符
     * 参数三：中间标题文字,需要隐藏时传null或空字符
     * 参数四五分别为右边
     *
     * @param
     */
    public void initHead(View view, int leftimage, String leftstr, String titlestr, int rightimage, String rightstr) {
        head_rl = (RelativeLayout) view.findViewById(R.id.head_rl);
        iv_return_left = (ImageView) view.findViewById(R.id.iv_return_left);
        lv_head_return = (LinearLayout) view.findViewById(R.id.lv_head_return);
        rl_head = (LinearLayout) view.findViewById(R.id.rl_head);
        head_right_ll = (LinearLayout) view.findViewById(R.id.head_right_ll);
        if (leftimage != 0) {
            iv_return_left.setVisibility(View.VISIBLE);
            iv_return_left.setBackgroundDrawable(getResources().getDrawable(leftimage));

        } else {
            iv_return_left.setVisibility(View.GONE);
        }
        tv_return_left = (TextView) view.findViewById(R.id.tv_return_left);
        if (!TextUtils.isEmpty(leftstr)) {
            tv_return_left.setVisibility(View.VISIBLE);
            tv_return_left.setText(leftstr);
        } else {
            tv_return_left.setVisibility(View.GONE);
        }
        tv_head_title = (TextView) view.findViewById(R.id.tv_head_title);
        if (!TextUtils.isEmpty(titlestr)) {
            tv_head_title.setVisibility(View.VISIBLE);
            tv_head_title.setText(titlestr);
        } else {
            tv_head_title.setVisibility(View.GONE);
        }
        iv_return_right = (ImageView) view.findViewById(R.id.iv_return_right);
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
        tv_return_right = (TextView) view.findViewById(R.id.tv_return_right);
        if (!TextUtils.isEmpty(rightstr)) {
            tv_return_right.setVisibility(View.VISIBLE);
            tv_return_right.setText(rightstr);
        } else {
            tv_return_right.setVisibility(View.GONE);
        }
        shopCartNum = (TextView) view.findViewById(R.id.shopCartNum);
    }

    public void initRefresh(SwipeToLoadLayout swipeToLoadLayout, View view) {
        if (swipeToLoadLayout != null && view != null) {
            CustomRefreshHeadView swipe_refresh_header = (CustomRefreshHeadView) view.findViewById(R.id.swipe_refresh_header);
            CustomDownEndView swipe_load_more_footer = (CustomDownEndView) view.findViewById(R.id.swipe_load_more_footer);
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
    public void setTitleColor(int color) {
        tv_head_title.setTextColor(color);
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

    public LinearLayout getRightLinearlayout() {
        return this.head_right_ll;
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
     * 检测所有ets是否为空
     */
    public boolean chkEditText(EditText... ets) {
        for (EditText et : ets) {
            String str = et.getText().toString();
            if (str == null || "".equals(str.trim())) {
                et.setError(et.getHint() + "不能为空");
                return false;
            }
        }
        return true;
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyBoard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            ((InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE))

                    .hideSoftInputFromWindow(activity.getCurrentFocus()

                                    .getWindowToken(),

                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWaittingDialog != null && mWaittingDialog.isShowing()) {
            mWaittingDialog.dismiss();
        }
        if (mMyOkhttp != null) {
            mMyOkhttp.cancel(this);
        }
    }
}