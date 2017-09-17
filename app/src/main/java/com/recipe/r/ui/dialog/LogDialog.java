package com.recipe.r.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.utils.ShowImageUtils;


/**
 * Created by Mark on 2016/5/23.
 * 创建菜谱弹窗
 */
public class LogDialog extends Dialog implements View.OnClickListener {
    private ImageView iv_menu_dialog;//菜谱图片
    private TextView title_menu_dialog;
    private TextView content_menu_dialog;
    //    private ImageView like_dialog;
    private Context context;
    private TextView price_menu_dialog;
    private TextView collection_dialog;
    private Button addcar_menu_dialog;
    private RadioButton rb_taste1;
    private RadioButton rb_taste2;
    private RadioButton rb_taste3;
    private OnItemClickListener mListener;

    public LogDialog(Context context) {
        this(context, R.style.MenuAlertDialog);
        this.context = context;
    }

    public LogDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_log);

        init();

        initViews();
        initListener();
    }


//    public void setLikeViews(int iv) {
//        like_dialog.setImageResource(iv);
//    }

    public void setBannerViews(String url) {
        ShowImageUtils.showImageView(context, R.mipmap.default_photo, url, iv_menu_dialog);
    }

    public void setTitle(String str) {
        title_menu_dialog.setText(str);
    }

    public void setContent(String str) {
        content_menu_dialog.setText(str);
    }

    private void init() {
        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        window.setAttributes(lp);
    }

    private void initViews() {
        title_menu_dialog = (TextView) findViewById(R.id.title_menu_dialog);
        content_menu_dialog = (TextView) findViewById(R.id.content_menu_dialog);
        iv_menu_dialog = (ImageView) findViewById(R.id.iv_menu_dialog);
        price_menu_dialog = (TextView) findViewById(R.id.price_menu_dialog);
        collection_dialog = (TextView) findViewById(R.id.collection_dialog);
        addcar_menu_dialog = (Button) findViewById(R.id.addcar_menu_dialog);
        rb_taste1 = (RadioButton) findViewById(R.id.rb_taste1);
        rb_taste2 = (RadioButton) findViewById(R.id.rb_taste2);
        rb_taste3 = (RadioButton) findViewById(R.id.rb_taste3);

//        like_dialog = (ImageView) findViewById(like_dialog);
    }

    private void initListener() {
        addcar_menu_dialog.setOnClickListener(this);
        collection_dialog.setOnClickListener(this);
        rb_taste1.setOnClickListener(this);
        rb_taste2.setOnClickListener(this);
        rb_taste3.setOnClickListener(this);
    }

    /**
     * 设置口味选择
     *
     * @param type
     */
    public void setClickRb(int type) {
        if (type == 0) {
            rb_taste1.setChecked(true);
            rb_taste1.setTextColor(context.getResources().getColor(R.color.white));
            rb_taste2.setChecked(false);
            rb_taste2.setTextColor(context.getResources().getColor(R.color.text_color));
            rb_taste3.setChecked(false);
            rb_taste3.setTextColor(context.getResources().getColor(R.color.text_color));
        } else if (type == 1) {
            rb_taste1.setChecked(false);
            rb_taste1.setTextColor(context.getResources().getColor(R.color.text_color));
            rb_taste2.setChecked(true);
            rb_taste2.setTextColor(context.getResources().getColor(R.color.white));
            rb_taste3.setChecked(false);
            rb_taste3.setTextColor(context.getResources().getColor(R.color.text_color));
        } else if (type == 2) {
            rb_taste1.setChecked(false);
            rb_taste1.setTextColor(context.getResources().getColor(R.color.text_color));
            rb_taste2.setChecked(false);
            rb_taste2.setTextColor(context.getResources().getColor(R.color.text_color));
            rb_taste3.setChecked(true);
            rb_taste3.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    public TextView getCollectionText() {
        if (collection_dialog != null) {
            return collection_dialog;
        }
        return null;
    }
//    public ImageView LikeView() {
//        if (like_dialog != null) {
//            return like_dialog;
//        }
//        return null;
//    }

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
