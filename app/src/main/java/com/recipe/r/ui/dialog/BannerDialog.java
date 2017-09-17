package com.recipe.r.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.recipe.r.R;
import com.recipe.r.utils.ShowImageUtils;

/**
 * 2017
 * 06
 * 2017/6/23
 * wangxiaoer
 * 功能描述：首页大图弹窗
 **/
public class BannerDialog extends Dialog {
    private ImageView iv_menu_dialog;
    private ImageView dialog_btn_back;
    private Context context;

    public BannerDialog(Context context) {
        this(context, R.style.MyAlertDialog);
        this.context = context;
    }

    public BannerDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_banner);
        init();
        initViews();
    }


    public void setBannerViews(Bitmap bitmap) {
        iv_menu_dialog.setImageBitmap(bitmap);
    }

    public void setImageViews(String url) {
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, url, iv_menu_dialog);
    }

    private void init() {
        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        window.setAttributes(lp);
    }

    private void initViews() {
        dialog_btn_back = (ImageView) findViewById(R.id.dialog_btn_back);
        iv_menu_dialog = (ImageView) findViewById(R.id.iv_menu_dialog);
        dialog_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}