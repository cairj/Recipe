package com.recipe.r.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.recipe.r.R;

/**
 * 2017
 * 06
 * 2017/6/13
 * wangxiaoer
 * 功能描述：
 **/
public class CustomDownEndView extends LinearLayout implements SwipeLoadMoreTrigger, SwipeTrigger {

    private TextView tvStatus;

    public CustomDownEndView(Context context) {
        this(context, null, 0);
    }


    public CustomDownEndView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDownEndView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //这个view随意定义  注意这里可以定义随意的底部我 这里只是图简单  公用了头
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(), R.layout.header, null);
        tvStatus = (TextView) view.findViewById(R.id.tvTest);
        addView(view, lp);
    }


    @Override
    public void onPrepare() {
        tvStatus.setText("上拉加载更多");
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {
        tvStatus.setText("上拉加载更多" + i);
    }


    @Override
    public void onRelease() {
        tvStatus.setText("松开释放");
    }

    @Override
    public void onComplete() {
        tvStatus.setText("加载完成");
    }


    @Override
    public void onReset() {
        tvStatus.setText("重新加载");
    }


    @Override
    public void onLoadMore() {
        tvStatus.setText("onLoadMore");
    }
}
