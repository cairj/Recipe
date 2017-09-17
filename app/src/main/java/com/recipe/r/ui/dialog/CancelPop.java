package com.recipe.r.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.recipe.r.R;

/**
 * Created by hj on 2017/6/21.
 * 取消付款弹窗
 */

public class CancelPop extends PopupWindow implements View.OnClickListener {

    private Button id_btn_no, id_btn_error, id_btn_stop, id_btn_other, id_btn_cancel;
    private View mPopView;
    private OnItemClickListener mListener;

    public CancelPop(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
        setPopupWindow();
        id_btn_no.setOnClickListener(this);
        id_btn_error.setOnClickListener(this);
        id_btn_stop.setOnClickListener(this);
        id_btn_other.setOnClickListener(this);
        id_btn_cancel.setOnClickListener(this);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.dialog_cancel_popup, null);
        id_btn_no = (Button) mPopView.findViewById(R.id.id_btn_no);
        id_btn_error = (Button) mPopView.findViewById(R.id.id_btn_error);
        id_btn_stop = (Button) mPopView.findViewById(R.id.id_btn_stop);
        id_btn_other = (Button) mPopView.findViewById(R.id.id_btn_other);
        id_btn_cancel = (Button) mPopView.findViewById(R.id.id_btn_cancel);
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }




    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }

}