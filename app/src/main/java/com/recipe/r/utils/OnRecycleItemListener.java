package com.recipe.r.utils;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * hht
 * recycleview的item点击操作
 */
public abstract class OnRecycleItemListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat gestureDetectorCompat;
    private RecyclerView recyclerView;
    public OnRecycleItemListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestrueListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        gestureDetectorCompat.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        gestureDetectorCompat.onTouchEvent(motionEvent);
    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh, int position);
    public abstract void onLongClick(RecyclerView.ViewHolder vh, int position);
    //OnGestureListener处理单击事件，OnDoubleTapListener处理双击事件
    //其中OnSingleTapConfirmed判断是单击还是双击事件，如果连续点击两次就是双击，如果只点击一次，系统等待一段时间后没有收到第二次点击事件则判断为单击，此时触发此事件
    //onDoubleTap双击事件
    //onDoubleTapEvent双击间隔中发生的动作
    private class ItemTouchHelperGestrueListener extends GestureDetector.SimpleOnGestureListener {
        //一次单独的轻击抬起事件，即普通点击事件
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                int position = vh.getLayoutPosition();
                onItemClick(vh,position);
            }
            return true;
        }
        //长按操作
        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
            if(child != null){
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                int position = vh.getLayoutPosition();
                onLongClick(vh,position);
            }
        }
        //用户按下屏幕就会触发
        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }
        //如果按下的事件超过瞬间，而且在按下的时候没有松开或者拖动，执行此方法
        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }
        //屏幕上拖动事件
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        //滑屏操作，用户按下触摸屏，快速移动后松开
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
