package com.recipe.r.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.recipe.r.R;

/**
 * 2017
 * 06
 * 2017/6/13
 * wangxiaoer
 * 功能描述：列表刷新自定义View
 **/
public class CustomRefreshHeadView extends LinearLayout implements SwipeRefreshTrigger, SwipeTrigger {

    private TextView tvStatus;

    public CustomRefreshHeadView(Context context) {
        this(context, null, 0);
    }


    public CustomRefreshHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRefreshHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //这个view随意定义

        //这里的原理就是简单的动态布局添加
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(), R.layout.header, null);
        addView(view, lp);
        tvStatus = (TextView) view.findViewById(R.id.tvTest);
    }

    @Override
    public void onRefresh() {
        tvStatus.setText("正在刷新");
    }

    @Override
    public void onPrepare() {
        tvStatus.setText("下拉刷新");
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {
        tvStatus.setText("下拉刷新" + i);
    }


    @Override
    public void onRelease() {
        tvStatus.setText("松开释放");
    }

    @Override
    public void onComplete() {
        tvStatus.setText("刷新完成");
    }


    @Override
    public void onReset() {
        tvStatus.setText("重新刷新");
    }


}
