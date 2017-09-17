package com.recipe.r.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.recipe.r.R;
import com.recipe.r.base.ConfigApp;

import java.util.ArrayList;

/**
 * 2017
 * 06
 * 2017/6/26
 * wangxiaoer
 * 功能描述：支付弹窗
 **/
public class PayWayDialog extends Dialog implements View.OnClickListener {

    ImageView recharge_dialog_close;
    ArrayList<RadioButton> checks;

    RadioButton recharge_zhifubao_cb;
    RadioButton recharge_wechat_cb;

    TextView dialogConfirmPay;

    private TextView price_pay;
    private View.OnClickListener onClickListener;

    /**
     * 区别三种支付方式 0:支付宝 1:微信支付
     **/
    public int payWay = ConfigApp.MY_WALLET;

    private Context context;

    /**
     * @param context
     * @param themeResId
     * @param onClickListener
     */
    public PayWayDialog(Context context, int themeResId, View.OnClickListener onClickListener) {
        super(context, themeResId);
        setContentView(R.layout.dialog_pay);
        this.context = context;
        this.onClickListener = onClickListener;
        init();
    }


    private void init() {
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = AbsListView.LayoutParams.MATCH_PARENT;
        lp.y = 0;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
        checks = new ArrayList<>();

        dialogConfirmPay = (TextView) findViewById(R.id.dialog_confirm_pay);
        recharge_zhifubao_cb = (RadioButton) findViewById(R.id.recharge_zhifubao_cb);
        recharge_wechat_cb = (RadioButton) findViewById(R.id.recharge_wechat_cb);
        recharge_dialog_close = (ImageView) findViewById(R.id.recharge_dialog_close);
        price_pay = (TextView) findViewById(R.id.price_pay);
        checks.add(recharge_zhifubao_cb);
        checks.add(recharge_wechat_cb);
        payWay = ConfigApp.ZHI_FU_BAO;
        dialogConfirmPay.setOnClickListener(onClickListener);
        recharge_dialog_close.setOnClickListener(this);
        recharge_zhifubao_cb.setOnClickListener(this);
        recharge_wechat_cb.setOnClickListener(this);
    }

    public void setPrice(String price) {
        price_pay.setText("¥" + price);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_dialog_close:
                dismiss();
                break;
            case R.id.recharge_zhifubao_cb:
                checkChanges(1);
                break;
            case R.id.recharge_wechat_cb:
                checkChanges(2);
                break;
        }
    }

    /**
     * 改变选中
     */
    private void checkChanges(int index) {
        for (int i = 0; i < checks.size(); i++) {
            checks.get(i).setChecked(false);
        }
        payWay = index;
        checks.get(index - 1).setChecked(true);
    }
}