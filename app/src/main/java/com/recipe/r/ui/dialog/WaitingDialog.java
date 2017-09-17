package com.recipe.r.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipe.r.R;


/**
 *
 * 等待对话框
 */

public class WaitingDialog extends AlertDialog {

    private ImageView imageViewAnimation = null;
    private TextView textViewInfo = null;
    private String TextTips = null;

    public WaitingDialog(Context context) {
        this(context, R.style.AlertDialogStyle2);
    }

    public WaitingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wait_dialog);
        setCanceledOnTouchOutside(true);
        initView();
        initWaitAnimation();
    }

    private void initView(){
        imageViewAnimation = (ImageView)findViewById(R.id.img_waiting);
        textViewInfo = (TextView) findViewById(R.id.text_view_wait_info);
        setInfoText();
    }

    private void setInfoText(){
        if(null != textViewInfo){
            if(TextUtils.isEmpty(TextTips)){
                textViewInfo.setText("");
                textViewInfo.setVisibility(View.GONE);
            }else{
                textViewInfo.setText(TextTips);
                textViewInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initWaitAnimation(){
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                imageViewAnimation.setImageResource(R.drawable.lading_live);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageViewAnimation
                        .getDrawable();
                animationDrawable.start();
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                imageViewAnimation.clearAnimation();
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
    }

    //设置显示信息
    public WaitingDialog setWaitText(String text){
        TextTips = text;
        setInfoText();
        return this;
    }

    public void showDialog(boolean show){
        if(show){
            show();
        }else{
            dismiss();
        }
    }
}
