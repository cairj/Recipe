package com.recipe.r.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.recipe.r.R;


/**
 * wangxiaoer
 * 功能：自定义进度条
 */
public class MyProgressDialog extends Dialog {

    private Context context;

    public MyProgressDialog(Context context) {
        super(context);
        this.context = context;
        setInit();
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        setInit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_myprogress);
        this.setCancelable(true);
    }


    private void setInit() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
    }


}
